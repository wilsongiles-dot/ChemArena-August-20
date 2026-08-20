package com.example.data.models

data class Question(
    val id: String,
    val topic: String,
    val subtopic: String = "General",
    val type: String, // "mcq" or "short"
    val q: String,
    val options: List<String> = emptyList(),
    val answerIndex: Int = 0,
    val answerShort: String = "",
    val explanation: String = "",
    val isPastExam: Boolean = false,
    val examYear: String = "",
    val analysisScenario: String = "",
    val analysisSteps: List<String> = emptyList(),
    val retrievalCue: String = ""
)

data class MemoryPair(
    val id: Int,
    val term: String,
    val definition: String,
    val topic: String = "General"
)

data class Player(
    val id: String = "",
    val name: String = "",
    val avatar: String = "🧪",
    val color: String = "#00E5FF",
    val score: Int = 0,
    val host: Boolean = false,
    val isBot: Boolean = false,
    val streak: Int = 0
)

data class GameRoom(
    val code: String = "",
    val hostId: String = "",
    val mode: String = "retrieval", // "retrieval", "analysis", "practice", "quiz"
    val status: String = "waiting", // "waiting", "playing", "results"
    val questions: List<Question> = emptyList(),
    val topics: List<String> = emptyList(),
    val currentQ: Int = 0,
    val players: Map<String, Player> = emptyMap(),
    val buzzedPlayerId: String? = null,
    val buzzedPlayerName: String? = null,
    val answeredBy: Map<String, String> = emptyMap(),
    val createdAt: Long = System.currentTimeMillis()
)

data class SubtopicMastery(
    val topic: String,
    val subtopic: String,
    val attempted: Int,
    val correct: Int,
    val accuracyPercent: Int
)

data class AchievementBadge(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val isUnlocked: Boolean,
    val progress: Float = 0f
)
