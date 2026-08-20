package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "match_records")
data class MatchRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val gameMode: String, // "retrieval", "analysis", "practice", "weak_spots", "quiz"
    val roomCode: String = "LOCAL",
    val isOnline: Boolean = false,
    val score: Int = 0,
    val totalQuestions: Int = 0,
    val correctCount: Int = 0,
    val rank: Int = 1,
    val opponentNames: String = "",
    val durationSeconds: Int = 0,
    val focusTopic: String = "All Topics"
)

@Entity(tableName = "topic_stats")
data class TopicStat(
    @PrimaryKey
    val topicName: String,
    val questionsAttempted: Int = 0,
    val questionsCorrect: Int = 0,
    val lastPlayedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "question_attempts")
data class QuestionAttempt(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val questionId: String,
    val topic: String,
    val subtopic: String,
    val isCorrect: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val mode: String = "practice"
)

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey
    val id: Int = 1,
    val playerName: String = "Chemist",
    val avatar: String = "🧪",
    val color: String = "#00E5FF",
    val totalMatches: Int = 0,
    val totalScore: Int = 0,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val lastActiveDate: String = "",
    val lastSyncTimestamp: Long = 0,
    val themeMode: String = "system", // "system", "dark", "light"
    val notificationsEnabled: Boolean = true,
    val reminderHour: Int = 18,
    val reminderMinute: Int = 0
)

@Entity(tableName = "bookmarked_questions")
data class BookmarkedQuestion(
    @PrimaryKey
    val questionId: String,
    val topic: String,
    val questionText: String,
    val answerText: String,
    val explanation: String,
    val timestamp: Long = System.currentTimeMillis()
)
