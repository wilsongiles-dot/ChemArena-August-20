package com.example

import com.example.data.models.QuestionsData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests for chemical data formatting errors, unescaped tags, broken indices,
 * and curriculum data integrity.
 */
class DataFormattingAndCurriculumTest {

    @Test
    fun testAllQuestionsAreFormattedCorrectly() {
        val questions = QuestionsData.ALL_QUESTIONS
        assertTrue("Questions bank should not be empty", questions.isNotEmpty())

        val seenIds = mutableSetOf<String>()

        questions.forEach { q ->
            // 1. Unique ID
            assertFalse("Duplicate question ID found: ${q.id}", seenIds.contains(q.id))
            seenIds.add(q.id)

            // 2. Question Text Formatting (q.q)
            assertFalse("Question text is empty for ${q.id}", q.q.isBlank())
            assertFalse("Question contains 'null' string in ${q.id}", q.q.contains("null"))
            assertFalse("Question contains 'undefined' in ${q.id}", q.q.contains("undefined"))
            assertFalse("Question contains raw HTML in ${q.id}", q.q.contains("<script>") || q.q.contains("</div>"))

            // 3. Topic and Subtopic
            assertTrue("Topic must be valid in ${q.id}", QuestionsData.TOPICS.contains(q.topic))
            assertFalse("Subtopic cannot be blank in ${q.id}", q.subtopic.isBlank())

            // 4. Options and Answer Index
            if (q.type == "mcq") {
                assertEquals("MCQ should have 4 options in ${q.id}", 4, q.options.size)
                assertTrue("Answer index out of bounds in ${q.id}: ${q.answerIndex}", q.answerIndex in 0..3)
                q.options.forEachIndexed { optIndex, opt ->
                    assertFalse("Option $optIndex is blank in ${q.id}", opt.isBlank())
                    assertFalse("Option $optIndex contains 'null' in ${q.id}", opt.contains("null"))
                }
            }

            // 5. Explanation Formatting
            assertFalse("Explanation is empty in ${q.id}", q.explanation.isBlank())
            assertFalse("Explanation contains unformatted error tokens in ${q.id}", q.explanation.contains("NaN") || q.explanation.contains("undefined"))

            // 6. Retrieval Cue Formatting
            assertFalse("Retrieval cue is empty in ${q.id}", q.retrievalCue.isBlank())

            // 7. Analysis Steps Formatting (if present)
            if (q.analysisSteps.isNotEmpty()) {
                q.analysisSteps.forEachIndexed { stepIdx, stepText ->
                    assertFalse("Analysis step $stepIdx description is blank in ${q.id}", stepText.isBlank())
                }
            }
        }
    }

    @Test
    fun testAchievementsFormattingAndProgress() {
        val achievements = QuestionsData.ACHIEVEMENTS_LIST
        assertTrue("Achievements list should not be empty", achievements.isNotEmpty())

        achievements.forEach { badge ->
            assertFalse("Badge ID should not be blank", badge.id.isBlank())
            assertFalse("Badge title should not be blank", badge.title.isBlank())
            assertFalse("Badge description should not be blank", badge.description.isBlank())
            assertFalse("Badge icon should not be blank", badge.icon.isBlank())
            assertTrue("Progress should be between 0 and 1", badge.progress in 0f..1f)
        }
    }

    @Test
    fun testCurriculumTopicsConsistency() {
        assertEquals("Curriculum should cover all 6 QCAA topics", 6, QuestionsData.TOPICS.size)
        assertTrue(QuestionsData.TOPICS.contains("Equilibrium"))
        assertTrue(QuestionsData.TOPICS.contains("Acids & Bases"))
        assertTrue(QuestionsData.TOPICS.contains("Redox"))
        assertTrue(QuestionsData.TOPICS.contains("Organic Chemistry"))
        assertTrue(QuestionsData.TOPICS.contains("Synthesis & Green Chem"))
        assertTrue(QuestionsData.TOPICS.contains("Macromolecules"))
    }
}
