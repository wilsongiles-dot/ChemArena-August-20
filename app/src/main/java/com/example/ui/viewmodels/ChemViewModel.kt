package com.example.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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
import com.example.data.repository.ChemRepository
import com.example.notifications.ReminderNotificationHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

data class MemoryCard(
    val id: Int,
    val text: String,
    val pairId: Int,
    val isFlipped: Boolean = false,
    val isMatched: Boolean = false
)

class ChemViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ChemRepository(application)

    val userProfile: StateFlow<UserProfile?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val matchHistory: StateFlow<List<MatchRecord>> = repository.allMatches
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val topicStats: StateFlow<List<TopicStat>> = repository.topicStats
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val questionAttempts: StateFlow<List<QuestionAttempt>> = repository.questionAttempts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookmarks: StateFlow<List<BookmarkedQuestion>> = repository.bookmarks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val struggledQuestionIds: StateFlow<List<String>> = repository.struggledQuestionIds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI Navigation State
    private val _currentScreen = MutableStateFlow(ScreenState.HOME)
    val currentScreen: StateFlow<ScreenState> = _currentScreen.asStateFlow()

    // Configuration Inputs
    val playerNameInput = MutableStateFlow("")
    val selectedAvatar = MutableStateFlow("🧪")
    val selectedColor = MutableStateFlow("#00E5FF")
    val selectedGameMode = MutableStateFlow("retrieval") // "retrieval", "analysis", "practice", "weak_spots", "quiz"
    val selectedQuestionCount = MutableStateFlow(10)
    val selectedTopics = MutableStateFlow<Set<String>>(QuestionsData.TOPICS.toSet())
    val curriculumPreset = MutableStateFlow("all")
    val isBotOpponent = MutableStateFlow(false)
    val joinRoomCodeInput = MutableStateFlow("")

    // Active Game / Learning State
    private val _roomCode = MutableStateFlow("")
    val roomCode: StateFlow<String> = _roomCode.asStateFlow()

    private val _isHost = MutableStateFlow(true)
    val isHost: StateFlow<Boolean> = _isHost.asStateFlow()

    private val _isOnlineGame = MutableStateFlow(false)
    val isOnlineGame: StateFlow<Boolean> = _isOnlineGame.asStateFlow()

    private val _activeQuestions = MutableStateFlow<List<Question>>(emptyList())
    val activeQuestions: StateFlow<List<Question>> = _activeQuestions.asStateFlow()

    private val _currentQIndex = MutableStateFlow(0)
    val currentQIndex: StateFlow<Int> = _currentQIndex.asStateFlow()

    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players.asStateFlow()

    private val _myPlayerId = MutableStateFlow("player_me")
    val myPlayerId: StateFlow<String> = _myPlayerId.asStateFlow()

    // Learning States
    private val _isAnswered = MutableStateFlow(false)
    val isAnswered: StateFlow<Boolean> = _isAnswered.asStateFlow()

    private val _isRetrievalRevealed = MutableStateFlow(false)
    val isRetrievalRevealed: StateFlow<Boolean> = _isRetrievalRevealed.asStateFlow()

    private val _analysisStepIndex = MutableStateFlow(0)
    val analysisStepIndex: StateFlow<Int> = _analysisStepIndex.asStateFlow()

    private val _selectedOptionIndex = MutableStateFlow<Int?>(null)
    val selectedOptionIndex: StateFlow<Int?> = _selectedOptionIndex.asStateFlow()

    private val _shortAnswerInput = MutableStateFlow("")
    val shortAnswerInput: StateFlow<String> = _shortAnswerInput

    private val _feedbackMessage = MutableStateFlow("")
    val feedbackMessage: StateFlow<String> = _feedbackMessage.asStateFlow()

    private val _isFeedbackCorrect = MutableStateFlow(false)
    val isFeedbackCorrect: StateFlow<Boolean> = _isFeedbackCorrect.asStateFlow()

    private val _practiceHintRevealed = MutableStateFlow(false)
    val practiceHintRevealed: StateFlow<Boolean> = _practiceHintRevealed.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // Compatibility state for quiz & game-play
    val isReadingPhase = MutableStateFlow(false)
    val buzzedPlayerName = MutableStateFlow<String?>(null)
    val buzzedPlayerId = MutableStateFlow<String?>(null)
    val timerSeconds = MutableStateFlow(30)
    val maxTimerSeconds = MutableStateFlow(30)

    // Memory cards state
    val memoryCards = MutableStateFlow<List<MemoryCard>>(emptyList())
    val memoryMoves = MutableStateFlow(0)
    val memoryMatchedPairs = MutableStateFlow(0)
    val memoryTimerSeconds = MutableStateFlow(0)

    // Diagnostic Weak-Area Calculations
    val subtopicMasteryList: StateFlow<List<SubtopicMastery>> = questionAttempts.combine(topicStats) { attempts, _ ->
        val map = mutableMapOf<String, Pair<Int, Int>>()
        attempts.forEach { att ->
            val key = "${att.topic}::${att.subtopic}"
            val curr = map[key] ?: Pair(0, 0)
            map[key] = Pair(curr.first + 1, curr.second + (if (att.isCorrect) 1 else 0))
        }
        map.map { (key, pair) ->
            val parts = key.split("::")
            val topic = parts[0]
            val subtopic = parts.getOrElse(1) { "General" }
            val acc = if (pair.first > 0) (pair.second * 100) / pair.first else 0
            SubtopicMastery(
                topic = topic,
                subtopic = subtopic,
                attempted = pair.first,
                correct = pair.second,
                accuracyPercent = acc
            )
        }.sortedBy { it.accuracyPercent }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val weakestTopics: StateFlow<List<String>> = topicStats.combine(subtopicMasteryList) { stats, subtopics ->
        val weakFromStats = stats.filter {
            it.questionsAttempted >= 2 && (it.questionsCorrect * 100 / it.questionsAttempted) < 70
        }.map { it.topicName }

        val weakFromSubtopics = subtopics.filter {
            it.attempted >= 2 && it.accuracyPercent < 70
        }.map { "${it.topic} (${it.subtopic})" }

        (weakFromStats + weakFromSubtopics).distinct()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val achievementBadges: StateFlow<List<AchievementBadge>> = combine(userProfile, topicStats, questionAttempts) { profile, stats, attempts ->
        val streak = profile?.currentStreak ?: 0
        val eqStat = stats.firstOrNull { it.topicName == "Equilibrium" }
        val eqAcc = if ((eqStat?.questionsAttempted ?: 0) >= 3) (eqStat!!.questionsCorrect * 100 / eqStat.questionsAttempted) else 0

        val abStat = stats.firstOrNull { it.topicName == "Acids & Bases" }
        val abAcc = if ((abStat?.questionsAttempted ?: 0) >= 3) (abStat!!.questionsCorrect * 100 / abStat.questionsAttempted) else 0

        val redoxStat = stats.firstOrNull { it.topicName == "Redox" }
        val redoxAcc = if ((redoxStat?.questionsAttempted ?: 0) >= 3) (redoxStat!!.questionsCorrect * 100 / redoxStat.questionsAttempted) else 0

        val orgStat = stats.firstOrNull { it.topicName == "Organic Chemistry" }
        val orgAcc = if ((orgStat?.questionsAttempted ?: 0) >= 3) (orgStat!!.questionsCorrect * 100 / orgStat.questionsAttempted) else 0

        QuestionsData.ACHIEVEMENTS_LIST.map { badge ->
            when (badge.id) {
                "ach_streak_3" -> badge.copy(isUnlocked = streak >= 3, progress = (streak / 3f).coerceIn(0f, 1f))
                "ach_eq_master" -> badge.copy(isUnlocked = eqAcc >= 80, progress = (eqAcc / 80f).coerceIn(0f, 1f))
                "ach_acid_spec" -> badge.copy(isUnlocked = abAcc >= 80, progress = (abAcc / 80f).coerceIn(0f, 1f))
                "ach_redox_ace" -> badge.copy(isUnlocked = redoxAcc >= 80, progress = (redoxAcc / 80f).coerceIn(0f, 1f))
                "ach_org_synth" -> badge.copy(isUnlocked = orgAcc >= 80, progress = (orgAcc / 80f).coerceIn(0f, 1f))
                "ach_analysis_pro" -> {
                    val count = attempts.count { it.mode == "analysis" }
                    badge.copy(isUnlocked = count >= 5, progress = (count / 5f).coerceIn(0f, 1f))
                }
                "ach_retrieval_champ" -> {
                    val count = attempts.count { it.mode == "retrieval" }
                    badge.copy(isUnlocked = count >= 10, progress = (count / 10f).coerceIn(0f, 1f))
                }
                else -> badge
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), QuestionsData.ACHIEVEMENTS_LIST)

    private var matchStartTime: Long = 0
    private var currentMatchCorrectCount = 0
    private val topicStatsMap = mutableMapOf<String, Pair<Int, Int>>()
    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            val profile = repository.getInitialisedProfile()
            playerNameInput.value = profile.playerName
            selectedAvatar.value = profile.avatar
            selectedColor.value = profile.color
        }
    }

    fun navigateTo(screen: ScreenState) {
        _currentScreen.value = screen
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
        viewModelScope.launch {
            delay(2800)
            if (_toastMessage.value == msg) _toastMessage.value = null
        }
    }

    fun setCurriculumFilter(filterKey: String) {
        curriculumPreset.value = filterKey
        val preset = when (filterKey) {
            "eq_only" -> setOf("Equilibrium")
            "eq_acid" -> setOf("Equilibrium", "Acids & Bases")
            "unit3" -> setOf("Equilibrium", "Acids & Bases", "Redox")
            "unit3_org" -> setOf("Equilibrium", "Acids & Bases", "Redox", "Organic Chemistry")
            "unit3_org_synth" -> setOf("Equilibrium", "Acids & Bases", "Redox", "Organic Chemistry", "Synthesis & Green Chem")
            else -> QuestionsData.TOPICS.toSet()
        }
        selectedTopics.value = preset
    }

    fun toggleTopic(topic: String) {
        val current = selectedTopics.value.toMutableSet()
        if (current.contains(topic)) {
            if (current.size > 1) {
                current.remove(topic)
                selectedTopics.value = current
            } else {
                showToast("Keep at least 1 topic selected")
            }
        } else {
            current.add(topic)
            selectedTopics.value = current
        }
    }

    // ==========================================
    // 1. ACTIVE RETRIEVAL PRACTICE LAUNCHER
    // ==========================================
    fun startRetrievalPractice(topicFilter: String? = null) {
        selectedGameMode.value = "retrieval"
        _isOnlineGame.value = false
        _isHost.value = true
        _roomCode.value = "RETRIEVAL"
        _myPlayerId.value = "player_me"

        val pool = QuestionsData.ALL_QUESTIONS
            .filter { if (topicFilter != null) it.topic == topicFilter else selectedTopics.value.contains(it.topic) }
            .shuffled()

        _activeQuestions.value = pool.take(selectedQuestionCount.value.coerceAtMost(pool.size))
        _currentQIndex.value = 0
        _isAnswered.value = false
        _isRetrievalRevealed.value = false
        _selectedOptionIndex.value = null
        _feedbackMessage.value = ""
        currentMatchCorrectCount = 0
        topicStatsMap.clear()
        matchStartTime = System.currentTimeMillis()

        setupSinglePlayer()
        navigateTo(ScreenState.RETRIEVAL_MODE)
    }

    // ==========================================
    // 2. DEEP CHEMISTRY ANALYSIS LAUNCHER
    // ==========================================
    fun startDeepAnalysisMode(topicFilter: String? = null) {
        selectedGameMode.value = "analysis"
        _isOnlineGame.value = false
        _isHost.value = true
        _roomCode.value = "ANALYSIS"
        _myPlayerId.value = "player_me"

        val pool = QuestionsData.ALL_QUESTIONS
            .filter { it.analysisSteps.isNotEmpty() }
            .filter { if (topicFilter != null) it.topic == topicFilter else selectedTopics.value.contains(it.topic) }
            .shuffled()

        _activeQuestions.value = if (pool.isNotEmpty()) pool.take(8) else QuestionsData.ALL_QUESTIONS.filter { it.analysisSteps.isNotEmpty() }
        _currentQIndex.value = 0
        _isAnswered.value = false
        _analysisStepIndex.value = 0
        _selectedOptionIndex.value = null
        _feedbackMessage.value = ""
        currentMatchCorrectCount = 0
        topicStatsMap.clear()
        matchStartTime = System.currentTimeMillis()

        setupSinglePlayer()
        navigateTo(ScreenState.ANALYSIS_MODE)
    }

    // ==========================================
    // 3. TARGETED WEAK-SPOTS PRACTICE LAUNCHER
    // ==========================================
    fun startWeakSpotsPractice() {
        selectedGameMode.value = "weak_spots"
        _isOnlineGame.value = false
        _isHost.value = true
        _roomCode.value = "DIAGNOSTIC"
        _myPlayerId.value = "player_me"

        val failedIds = struggledQuestionIds.value.toSet()
        val weakTopics = weakestTopics.value

        val weakQuestions = QuestionsData.ALL_QUESTIONS.filter { q ->
            failedIds.contains(q.id) || weakTopics.any { it.contains(q.topic) || it.contains(q.subtopic) }
        }.shuffled()

        val questions = if (weakQuestions.isNotEmpty()) {
            weakQuestions.take(8)
        } else {
            QuestionsData.ALL_QUESTIONS.shuffled().take(8)
        }

        _activeQuestions.value = questions
        _currentQIndex.value = 0
        _isAnswered.value = false
        _selectedOptionIndex.value = null
        _feedbackMessage.value = ""
        currentMatchCorrectCount = 0
        topicStatsMap.clear()
        matchStartTime = System.currentTimeMillis()

        setupSinglePlayer()
        navigateTo(ScreenState.WEAK_SPOTS_MODE)
    }

    // ==========================================
    // 4. UNTIMED LAB PRACTICE
    // ==========================================
    fun startPracticeMode() {
        selectedGameMode.value = "practice"
        _isOnlineGame.value = false
        _isHost.value = true
        _roomCode.value = "PRACTICE"
        _myPlayerId.value = "player_me"

        val pool = QuestionsData.ALL_QUESTIONS
            .filter { selectedTopics.value.contains(it.topic) }
            .shuffled()

        _activeQuestions.value = pool.take(selectedQuestionCount.value.coerceAtMost(pool.size))
        _currentQIndex.value = 0
        _isAnswered.value = false
        _selectedOptionIndex.value = null
        _feedbackMessage.value = ""
        _practiceHintRevealed.value = false
        currentMatchCorrectCount = 0
        topicStatsMap.clear()
        matchStartTime = System.currentTimeMillis()

        setupSinglePlayer()
        navigateTo(ScreenState.PRACTICE)
    }

    private fun setupSinglePlayer() {
        val me = Player(
            id = "player_me",
            name = playerNameInput.value.ifBlank { "Chemist" },
            avatar = selectedAvatar.value,
            color = selectedColor.value,
            score = 0,
            host = true
        )
        _players.value = listOf(me)
    }

    // ==========================================
    // LEARNING INTERACTION HANDLERS
    // ==========================================

    // Active Retrieval Handlers
    fun revealRetrievalOptions() {
        _isRetrievalRevealed.value = true
    }

    fun submitRetrievalOption(index: Int) {
        if (_isAnswered.value) return
        val q = _activeQuestions.value.getOrNull(_currentQIndex.value) ?: return

        _isAnswered.value = true
        _selectedOptionIndex.value = index

        val isCorrect = index == q.answerIndex
        _isFeedbackCorrect.value = isCorrect

        if (isCorrect) {
            _feedbackMessage.value = "✅ Concept Retrieved Correctly!\n${q.explanation}"
            currentMatchCorrectCount++
        } else {
            val correctAns = q.options.getOrElse(q.answerIndex) { "" }
            _feedbackMessage.value = "❌ Review Required: Correct answer is \"$correctAns\".\n${q.explanation}"
        }

        recordLearningAttempt(q, isCorrect, "retrieval")
    }

    fun submitRetrievalSelfEvaluation(rememberedCorrectly: Boolean) {
        val q = _activeQuestions.value.getOrNull(_currentQIndex.value) ?: return
        _isAnswered.value = true
        _isFeedbackCorrect.value = rememberedCorrectly

        if (rememberedCorrectly) {
            _feedbackMessage.value = "🧠 Excellent Active Recall!\n${q.explanation}"
            currentMatchCorrectCount++
        } else {
            _feedbackMessage.value = "💡 Added to your Weak-Spots Review Queue.\n${q.explanation}"
        }

        recordLearningAttempt(q, rememberedCorrectly, "retrieval")
    }

    fun nextRetrievalQuestion() {
        _isAnswered.value = false
        _isRetrievalRevealed.value = false
        _selectedOptionIndex.value = null
        _feedbackMessage.value = ""

        val next = _currentQIndex.value + 1
        if (next >= _activeQuestions.value.size) {
            endLearningSession("Active Retrieval")
        } else {
            _currentQIndex.value = next
        }
    }

    // Deep Analysis Handlers
    fun advanceAnalysisStep() {
        val q = _activeQuestions.value.getOrNull(_currentQIndex.value) ?: return
        if (_analysisStepIndex.value < q.analysisSteps.size - 1) {
            _analysisStepIndex.value = _analysisStepIndex.value + 1
        }
    }

    fun submitAnalysisOption(index: Int) {
        if (_isAnswered.value) return
        val q = _activeQuestions.value.getOrNull(_currentQIndex.value) ?: return

        _isAnswered.value = true
        _selectedOptionIndex.value = index

        val isCorrect = index == q.answerIndex
        _isFeedbackCorrect.value = isCorrect

        if (isCorrect) {
            _feedbackMessage.value = "🎯 Accurate Analytical Deduction!\n${q.explanation}"
            currentMatchCorrectCount++
        } else {
            val correctAns = q.options.getOrElse(q.answerIndex) { "" }
            _feedbackMessage.value = "🔍 Analytical Check: Expected \"$correctAns\".\n${q.explanation}"
        }

        recordLearningAttempt(q, isCorrect, "analysis")
    }

    fun nextAnalysisQuestion() {
        _isAnswered.value = false
        _analysisStepIndex.value = 0
        _selectedOptionIndex.value = null
        _feedbackMessage.value = ""

        val next = _currentQIndex.value + 1
        if (next >= _activeQuestions.value.size) {
            endLearningSession("Deep Analysis")
        } else {
            _currentQIndex.value = next
        }
    }

    // Weak Spots Handlers
    fun submitWeakSpotOption(index: Int) {
        if (_isAnswered.value) return
        val q = _activeQuestions.value.getOrNull(_currentQIndex.value) ?: return

        _isAnswered.value = true
        _selectedOptionIndex.value = index

        val isCorrect = index == q.answerIndex
        _isFeedbackCorrect.value = isCorrect

        if (isCorrect) {
            _feedbackMessage.value = "🌟 Weak-Spot Mastered!\n${q.explanation}"
            currentMatchCorrectCount++
        } else {
            val correctAns = q.options.getOrElse(q.answerIndex) { "" }
            _feedbackMessage.value = "⚠️ Review Again: Expected \"$correctAns\".\n${q.explanation}"
        }

        recordLearningAttempt(q, isCorrect, "weak_spots")
    }

    fun nextWeakSpotQuestion() {
        _isAnswered.value = false
        _selectedOptionIndex.value = null
        _feedbackMessage.value = ""

        val next = _currentQIndex.value + 1
        if (next >= _activeQuestions.value.size) {
            endLearningSession("Weak Spots")
        } else {
            _currentQIndex.value = next
        }
    }

    // Practice Mode Actions
    fun submitPracticeOption(optionIndex: Int) {
        val q = _activeQuestions.value.getOrNull(_currentQIndex.value) ?: return
        val isCorrect = optionIndex == q.answerIndex
        _selectedOptionIndex.value = optionIndex

        if (isCorrect) {
            _isAnswered.value = true
            _isFeedbackCorrect.value = true
            _feedbackMessage.value = "✅ Correct! ${q.explanation}"
            currentMatchCorrectCount++
            recordLearningAttempt(q, isCorrect = true, mode = "practice")
        } else {
            _practiceHintRevealed.value = true
            _isFeedbackCorrect.value = false
            _feedbackMessage.value = "💡 Hint: ${getHint(q)} (Try analyzing the question again!)"
            recordLearningAttempt(q, isCorrect = false, mode = "practice")
        }
    }

    fun nextPracticeQuestion() {
        _practiceHintRevealed.value = false
        _isAnswered.value = false
        _selectedOptionIndex.value = null
        _feedbackMessage.value = ""
        val next = _currentQIndex.value + 1
        if (next >= _activeQuestions.value.size) {
            endLearningSession("Lab Practice")
        } else {
            _currentQIndex.value = next
        }
    }

    private fun recordLearningAttempt(q: Question, isCorrect: Boolean, mode: String) {
        viewModelScope.launch {
            repository.recordAttempt(
                questionId = q.id,
                topic = q.topic,
                subtopic = q.subtopic,
                isCorrect = isCorrect,
                mode = mode
            )
        }
        val current = topicStatsMap[q.topic] ?: Pair(0, 0)
        topicStatsMap[q.topic] = Pair(current.first + 1, current.second + (if (isCorrect) 1 else 0))
    }

    private fun getHint(q: Question): String {
        val correct = if (q.type == "mcq" && q.options.isNotEmpty()) q.options[q.answerIndex] else q.answerShort
        val cleanExp = q.explanation.replace(Regex(Regex.escape(correct), RegexOption.IGNORE_CASE), "___")
        return cleanExp.split(".").firstOrNull()?.plus(".") ?: "Think about the equilibrium and molecular principles involved."
    }

    private fun endLearningSession(sessionType: String) {
        val total = _activeQuestions.value.size
        val duration = ((System.currentTimeMillis() - matchStartTime) / 1000).toInt().coerceAtLeast(10)
        val score = currentMatchCorrectCount * 10

        viewModelScope.launch {
            repository.recordMatch(
                gameMode = selectedGameMode.value,
                roomCode = _roomCode.value,
                isOnline = false,
                score = score,
                totalQuestions = total,
                correctCount = currentMatchCorrectCount,
                rank = 1,
                opponentNames = "",
                durationSeconds = duration,
                focusTopic = selectedTopics.value.joinToString(", ").take(40),
                topicBreakdown = topicStatsMap
            )
        }

        navigateTo(ScreenState.RESULTS)
    }

    fun leaveMatch() {
        _roomCode.value = ""
        timerJob?.cancel()
        navigateTo(ScreenState.HOME)
    }

    // Compatibility methods for secondary multiplayer / quiz / memory screens
    fun getCurrentTurnPlayer(): Player? = _players.value.firstOrNull()
    fun revealOptionsAndStartTimer() { isReadingPhase.value = false }
    fun onBuzzIn() { buzzedPlayerId.value = _myPlayerId.value }
    fun submitOption(idx: Int) { submitRetrievalOption(idx) }
    fun submitShortAnswer(ans: String = "") {}
    fun startOfflineGame() { startRetrievalPractice() }
    fun createOnlineRoom() { startRetrievalPractice() }
    fun joinOnlineRoom(code: String = "") { startRetrievalPractice() }
    fun startOnlineGameFromWaiting() { startRetrievalPractice() }
    fun onMemoryCardClick(id: Int) {}

    // Bookmarks
    fun toggleBookmark(question: Question) {
        viewModelScope.launch {
            repository.toggleBookmark(question)
            showToast("Question saved to bookmarks! 📚")
        }
    }

    fun removeBookmark(qId: String) {
        viewModelScope.launch {
            repository.removeBookmark(qId)
            showToast("Bookmark removed")
        }
    }

    // Cloud Sync
    fun backupProfile() {
        viewModelScope.launch {
            val success = repository.backupProfileToCloud()
            if (success) showToast("Student achievements synced to cloud! ☁️") else showToast("Failed to backup.")
        }
    }

    fun restoreProfile(cloudUserId: String) {
        viewModelScope.launch {
            val success = repository.restoreProfileFromCloud(cloudUserId)
            if (success) showToast("Profile restored! ☁️") else showToast("No cloud save found for $cloudUserId")
        }
    }

    fun updateThemeMode(mode: String) {
        viewModelScope.launch {
            val profile = repository.getInitialisedProfile()
            repository.updateProfile(profile.copy(themeMode = mode))
        }
    }

    fun updateProfileInfo(name: String, avatar: String, color: String) {
        playerNameInput.value = name
        selectedAvatar.value = avatar
        selectedColor.value = color
        viewModelScope.launch {
            val profile = repository.getInitialisedProfile()
            repository.updateProfile(profile.copy(playerName = name, avatar = avatar, color = color))
            showToast("Profile updated!")
        }
    }

    fun testReminderNotification() {
        val streak = userProfile.value?.currentStreak ?: 1
        ReminderNotificationHelper.showStudyReminder(getApplication(), streak)
        showToast("Study reminder alert sent! Check notifications.")
    }
}
