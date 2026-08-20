package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {
    @Query("SELECT * FROM match_records ORDER BY timestamp DESC")
    fun getAllMatches(): Flow<List<MatchRecord>>

    @Query("SELECT * FROM match_records ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentMatches(limit: Int): Flow<List<MatchRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: MatchRecord): Long

    @Query("DELETE FROM match_records")
    suspend fun clearAllMatches()
}

@Dao
interface TopicStatDao {
    @Query("SELECT * FROM topic_stats")
    fun getAllTopicStats(): Flow<List<TopicStat>>

    @Query("SELECT * FROM topic_stats WHERE topicName = :topic LIMIT 1")
    suspend fun getStatForTopic(topic: String): TopicStat?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateStat(stat: TopicStat)

    @Query("UPDATE topic_stats SET questionsAttempted = questionsAttempted + :attempted, questionsCorrect = questionsCorrect + :correct, lastPlayedTimestamp = :time WHERE topicName = :topic")
    suspend fun incrementStat(topic: String, attempted: Int, correct: Int, time: Long)
}

@Dao
interface QuestionAttemptDao {
    @Query("SELECT * FROM question_attempts ORDER BY timestamp DESC")
    fun getAllAttempts(): Flow<List<QuestionAttempt>>

    @Query("SELECT * FROM question_attempts WHERE isCorrect = 0 ORDER BY timestamp DESC")
    fun getFailedAttempts(): Flow<List<QuestionAttempt>>

    @Query("SELECT DISTINCT questionId FROM question_attempts WHERE isCorrect = 0")
    fun getStruggledQuestionIds(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(attempt: QuestionAttempt): Long

    @Query("DELETE FROM question_attempts")
    suspend fun clearAttempts()
}

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getUserProfileOnce(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfile)

    @Update
    suspend fun updateProfile(profile: UserProfile)
}

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmarked_questions ORDER BY timestamp DESC")
    fun getAllBookmarks(): Flow<List<BookmarkedQuestion>>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarked_questions WHERE questionId = :qId)")
    fun isBookmarked(qId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookmark(bookmark: BookmarkedQuestion)

    @Query("DELETE FROM bookmarked_questions WHERE questionId = :qId")
    suspend fun removeBookmark(qId: String)
}
