package com.example.data.repository

import android.content.Context
import com.example.data.db.AppDatabase
import com.example.data.db.BookmarkedQuestion
import com.example.data.db.MatchRecord
import com.example.data.db.QuestionAttempt
import com.example.data.db.TopicStat
import com.example.data.db.UserProfile
import com.example.data.models.AchievementBadge
import com.example.data.models.GameRoom
import com.example.data.models.Player
import com.example.data.models.Question
import com.example.data.models.QuestionsData
import com.example.data.models.SubtopicMastery
import com.example.data.remote.FirebaseClient
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChemRepository(context: Context) {
    private val db = AppDatabase.getDatabase(context)
    private val matchDao = db.matchDao()
    private val topicStatDao = db.topicStatDao()
    private val attemptDao = db.questionAttemptDao()
    private val userProfileDao = db.userProfileDao()
    private val bookmarkDao = db.bookmarkDao()
    private val firebaseClient = FirebaseClient()

    val userProfile: Flow<UserProfile?> = userProfileDao.getUserProfile()
    val allMatches: Flow<List<MatchRecord>> = matchDao.getAllMatches()
    val topicStats: Flow<List<TopicStat>> = topicStatDao.getAllTopicStats()
    val bookmarks: Flow<List<BookmarkedQuestion>> = bookmarkDao.getAllBookmarks()
    val questionAttempts: Flow<List<QuestionAttempt>> = attemptDao.getAllAttempts()
    val failedAttempts: Flow<List<QuestionAttempt>> = attemptDao.getFailedAttempts()
    val struggledQuestionIds: Flow<List<String>> = attemptDao.getStruggledQuestionIds()

    suspend fun getInitialisedProfile(): UserProfile {
        var profile = userProfileDao.getUserProfileOnce()
        if (profile == null) {
            val randomAvatar = QuestionsData.AVATARS.random()
            val randomColor = QuestionsData.COLORS.random()
            profile = UserProfile(
                id = 1,
                playerName = "Chemist_${(100..999).random()}",
                avatar = randomAvatar,
                color = randomColor,
                lastActiveDate = getTodayDateString(),
                currentStreak = 1,
                bestStreak = 1
            )
            userProfileDao.insertOrUpdateProfile(profile)
        }
        return profile
    }

    suspend fun updateProfile(profile: UserProfile) {
        userProfileDao.insertOrUpdateProfile(profile)
    }

    suspend fun recordAttempt(
        questionId: String,
        topic: String,
        subtopic: String,
        isCorrect: Boolean,
        mode: String = "practice"
    ) {
        attemptDao.insertAttempt(
            QuestionAttempt(
                questionId = questionId,
                topic = topic,
                subtopic = subtopic,
                isCorrect = isCorrect,
                mode = mode
            )
        )

        // Increment topic stats
        val now = System.currentTimeMillis()
        val existing = topicStatDao.getStatForTopic(topic)
        if (existing == null) {
            topicStatDao.insertOrUpdateStat(
                TopicStat(
                    topicName = topic,
                    questionsAttempted = 1,
                    questionsCorrect = if (isCorrect) 1 else 0,
                    lastPlayedTimestamp = now
                )
            )
        } else {
            topicStatDao.incrementStat(topic, 1, if (isCorrect) 1 else 0, now)
        }
    }

    suspend fun recordMatch(
        gameMode: String,
        roomCode: String,
        isOnline: Boolean,
        score: Int,
        totalQuestions: Int,
        correctCount: Int,
        rank: Int,
        opponentNames: String,
        durationSeconds: Int,
        focusTopic: String = "All Topics",
        topicBreakdown: Map<String, Pair<Int, Int>> = emptyMap()
    ) {
        val match = MatchRecord(
            gameMode = gameMode,
            roomCode = roomCode,
            isOnline = isOnline,
            score = score,
            totalQuestions = totalQuestions,
            correctCount = correctCount,
            rank = rank,
            opponentNames = opponentNames,
            durationSeconds = durationSeconds,
            focusTopic = focusTopic
        )
        matchDao.insertMatch(match)

        // Update User Profile Stats & Streak
        val currentProfile = getInitialisedProfile()
        val today = getTodayDateString()
        val yesterday = getYesterdayDateString()

        val newStreak = when (currentProfile.lastActiveDate) {
            today -> currentProfile.currentStreak
            yesterday -> currentProfile.currentStreak + 1
            "" -> 1
            else -> 1
        }
        val bestStreak = maxOf(newStreak, currentProfile.bestStreak)

        val updatedProfile = currentProfile.copy(
            totalMatches = currentProfile.totalMatches + 1,
            totalScore = currentProfile.totalScore + score,
            currentStreak = newStreak,
            bestStreak = bestStreak,
            lastActiveDate = today
        )
        userProfileDao.insertOrUpdateProfile(updatedProfile)
    }

    suspend fun toggleBookmark(question: Question) {
        bookmarkDao.addBookmark(
            BookmarkedQuestion(
                questionId = question.id,
                topic = question.topic,
                questionText = question.q,
                answerText = if (question.type == "mcq" && question.options.isNotEmpty()) question.options[question.answerIndex] else question.answerShort,
                explanation = question.explanation
            )
        )
    }

    suspend fun removeBookmark(questionId: String) {
        bookmarkDao.removeBookmark(questionId)
    }

    // Remote Firebase calls
    suspend fun createOnlineRoom(roomCode: String, room: GameRoom): Boolean {
        return firebaseClient.createOrUpdateRoom(roomCode, room)
    }

    suspend fun joinOnlineRoom(roomCode: String, player: Player): GameRoom? {
        val room = firebaseClient.getRoom(roomCode) ?: return null
        if (room.status != "waiting") return null
        if (room.players.size >= 8) return null
        val added = firebaseClient.addPlayer(roomCode, player)
        if (!added) return null
        return firebaseClient.getRoom(roomCode)
    }

    suspend fun pollRoom(roomCode: String): GameRoom? {
        return firebaseClient.getRoom(roomCode)
    }

    suspend fun buzzIn(roomCode: String, playerId: String, playerName: String): Boolean {
        return firebaseClient.buzzIn(roomCode, playerId, playerName)
    }

    suspend fun claimAnswer(roomCode: String, questionIndex: Int, playerId: String): Boolean {
        return firebaseClient.claimAnswer(roomCode, questionIndex, playerId)
    }

    suspend fun updatePlayerScore(roomCode: String, playerId: String, score: Int): Boolean {
        return firebaseClient.updatePlayerScore(roomCode, playerId, score)
    }

    suspend fun updateRoomStatus(roomCode: String, status: String, currentQ: Int = 0): Boolean {
        return firebaseClient.createOrUpdateRoom(
            roomCode,
            (firebaseClient.getRoom(roomCode) ?: return false).copy(status = status, currentQ = currentQ)
        )
    }

    suspend fun advanceRoomQuestion(roomCode: String, nextQ: Int): Boolean {
        return firebaseClient.updateRoomField(roomCode, "currentQ", nextQ)
    }

    suspend fun leaveRoom(roomCode: String, playerId: String) {
        firebaseClient.leaveRoom(roomCode, playerId)
    }

    // Cloud Save Sync
    suspend fun backupProfileToCloud(): Boolean {
        val profile = getInitialisedProfile()
        val json = JSONObject().apply {
            put("playerName", profile.playerName)
            put("avatar", profile.avatar)
            put("color", profile.color)
            put("totalMatches", profile.totalMatches)
            put("totalScore", profile.totalScore)
            put("currentStreak", profile.currentStreak)
            put("bestStreak", profile.bestStreak)
            put("lastActiveDate", profile.lastActiveDate)
            put("timestamp", System.currentTimeMillis())
        }
        val success = firebaseClient.syncCloudProfile(profile.playerName, json)
        if (success) {
            userProfileDao.insertOrUpdateProfile(profile.copy(lastSyncTimestamp = System.currentTimeMillis()))
        }
        return success
    }

    suspend fun restoreProfileFromCloud(cloudUserId: String): Boolean {
        val json = firebaseClient.getCloudProfile(cloudUserId) ?: return false
        val current = getInitialisedProfile()
        val restored = current.copy(
            playerName = json.optString("playerName", current.playerName),
            avatar = json.optString("avatar", current.avatar),
            color = json.optString("color", current.color),
            totalMatches = json.optInt("totalMatches", current.totalMatches),
            totalScore = json.optInt("totalScore", current.totalScore),
            currentStreak = json.optInt("currentStreak", current.currentStreak),
            bestStreak = json.optInt("bestStreak", current.bestStreak),
            lastActiveDate = json.optString("lastActiveDate", current.lastActiveDate),
            lastSyncTimestamp = System.currentTimeMillis()
        )
        userProfileDao.insertOrUpdateProfile(restored)
        return true
    }

    private fun getTodayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    private fun getYesterdayDateString(): String {
        val yesterday = Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000)
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(yesterday)
    }
}
