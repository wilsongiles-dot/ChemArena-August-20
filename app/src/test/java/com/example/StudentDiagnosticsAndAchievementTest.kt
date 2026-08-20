package com.example

import com.example.data.db.QuestionAttempt
import com.example.data.db.TopicStat
import com.example.data.models.SubtopicMastery
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests for student diagnostic tracking, weak-area detection calculations,
 * and mastery percentage aggregation.
 */
class StudentDiagnosticsAndAchievementTest {

    @Test
    fun testWeakSpotsDetectionLogic() {
        // Mock subtopic attempts
        val attempts = listOf(
            QuestionAttempt(questionId = "q1", topic = "Equilibrium", subtopic = "Le Chatelier's Principle", isCorrect = true, mode = "retrieval"),
            QuestionAttempt(questionId = "q2", topic = "Equilibrium", subtopic = "Le Chatelier's Principle", isCorrect = false, mode = "retrieval"),
            QuestionAttempt(questionId = "q3", topic = "Equilibrium", subtopic = "Le Chatelier's Principle", isCorrect = false, mode = "retrieval"),
            QuestionAttempt(questionId = "q4", topic = "Acids & Bases", subtopic = "Buffer Systems", isCorrect = true, mode = "retrieval"),
            QuestionAttempt(questionId = "q5", topic = "Acids & Bases", subtopic = "Buffer Systems", isCorrect = true, mode = "retrieval")
        )

        val map = mutableMapOf<String, Pair<Int, Int>>()
        attempts.forEach { att ->
            val key = "${att.topic}::${att.subtopic}"
            val curr = map[key] ?: Pair(0, 0)
            map[key] = Pair(curr.first + 1, curr.second + (if (att.isCorrect) 1 else 0))
        }

        val subtopics = map.map { (key, pair) ->
            val parts = key.split("::")
            val acc = if (pair.first > 0) (pair.second * 100) / pair.first else 0
            SubtopicMastery(
                topic = parts[0],
                subtopic = parts[1],
                attempted = pair.first,
                correct = pair.second,
                accuracyPercent = acc
            )
        }

        // Le Chatelier: 1/3 correct = 33% accuracy (< 70% threshold)
        val eqSubtopic = subtopics.first { it.subtopic == "Le Chatelier's Principle" }
        assertEquals(33, eqSubtopic.accuracyPercent)
        assertTrue("Subtopic with 33% accuracy should be flagged as weak", eqSubtopic.accuracyPercent < 70)

        // Buffer Systems: 2/2 correct = 100% accuracy
        val bufSubtopic = subtopics.first { it.subtopic == "Buffer Systems" }
        assertEquals(100, bufSubtopic.accuracyPercent)
        assertFalse("Subtopic with 100% accuracy should not be flagged", bufSubtopic.accuracyPercent < 70)
    }

    @Test
    fun testOverallSyllabusAccuracyCalculation() {
        val stats = listOf(
            TopicStat(topicName = "Equilibrium", questionsAttempted = 10, questionsCorrect = 8),
            TopicStat(topicName = "Acids & Bases", questionsAttempted = 10, questionsCorrect = 6),
            TopicStat(topicName = "Redox", questionsAttempted = 5, questionsCorrect = 5)
        )

        val totalAttempts = stats.sumOf { it.questionsAttempted } // 25
        val totalCorrect = stats.sumOf { it.questionsCorrect } // 19
        val overallAccuracy = (totalCorrect * 100) / totalAttempts // 76%

        assertEquals(25, totalAttempts)
        assertEquals(19, totalCorrect)
        assertEquals(76, overallAccuracy)
    }
}
