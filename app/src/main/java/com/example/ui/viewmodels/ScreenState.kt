package com.example.ui.viewmodels

enum class ScreenState {
    HOME,
    RETRIEVAL_MODE,    // Active Recall Flashcards & Principle Retrieval
    ANALYSIS_MODE,     // Deep Multi-Step Scenario Problem Solving
    WEAK_SPOTS_MODE,   // Targeted Diagnostic Practice on Weakest Concepts
    PRACTICE,          // Self-paced study with instant hints
    GAME_PLAY,         // Classic quiz battle
    MEMORY_MATCH,      // Chemical formula & terms match
    QUESTION_BANK,     // Searchable QCAA question archive
    ANALYTICS,         // Diagnostic Weak Area Analysis & Student Achievements
    SETTINGS,
    CREATE_ROOM,
    JOIN_ROOM,
    WAITING_ROOM,
    RESULTS
}
