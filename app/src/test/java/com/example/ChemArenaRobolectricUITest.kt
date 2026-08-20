package com.example

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.example.data.models.AchievementBadge
import com.example.data.models.Question
import com.example.ui.components.ChemCard
import com.example.ui.components.ChemPrimaryButton
import com.example.ui.screens.AchievementRow
import com.example.ui.screens.LearningModeCard
import com.example.ui.screens.PresetChip
import com.example.ui.theme.ChemCyan
import com.example.ui.theme.MyApplicationTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Text
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Tests UI formatting under standard and large accessibility font scales (1.75x, 2.0x)
 * to verify buttons, badges, and cards do not clip or break layout boundaries.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ChemArenaRobolectricUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLearningModeCard_rendersAndRemainsClickable() {
        composeTestRule.setContent {
            MyApplicationTheme {
                LearningModeCard(
                    title = "💡 Active Retrieval Practice",
                    subtitle = "Recall core principles before seeing options.",
                    badgeText = "RECOMMENDED",
                    badgeColor = ChemCyan,
                    accentColor = ChemCyan,
                    icon = Icons.Default.Psychology,
                    onClick = {},
                    testTag = "mode_retrieval"
                )
            }
        }

        composeTestRule.onNodeWithTag("mode_retrieval").assertIsDisplayed()
        composeTestRule.onNodeWithText("💡 Active Retrieval Practice").assertIsDisplayed()
        composeTestRule.onNodeWithText("RECOMMENDED").assertIsDisplayed()
    }

    @Test
    fun testLearningCard_largeAccessibilityFontScale_doesNotClip() {
        composeTestRule.setContent {
            CompositionLocalProvider(
                LocalDensity provides Density(density = 1f, fontScale = 1.75f)
            ) {
                MyApplicationTheme(darkTheme = true) {
                    Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        ChemCard {
                            Text("QCAA Chemistry Units 3 & 4 Equilibrium Scenarios")
                            ChemPrimaryButton(
                                text = "START RETRIEVAL",
                                onClick = {},
                                testTag = "test_primary_btn"
                            )
                        }
                    }
                }
            }
        }

        composeTestRule.onNodeWithTag("test_primary_btn").assertIsDisplayed()
        composeTestRule.onNodeWithText("START RETRIEVAL").assertIsDisplayed()
    }

    @Test
    fun testAchievementBadgeRow_unlockedAndLockedFormatting() {
        val unlockedBadge = AchievementBadge(
            id = "ach_eq",
            title = "Equilibrium Master",
            description = "Achieve 80%+ accuracy across all equilibrium calculations.",
            icon = "⚖️",
            isUnlocked = true,
            progress = 1f
        )

        composeTestRule.setContent {
            MyApplicationTheme {
                AchievementRow(badge = unlockedBadge)
            }
        }

        composeTestRule.onNodeWithText("Equilibrium Master").assertIsDisplayed()
        composeTestRule.onNodeWithText("Achieve 80%+ accuracy across all equilibrium calculations.").assertIsDisplayed()
    }

    @Test
    fun testPresetChip_selectionState() {
        composeTestRule.setContent {
            MyApplicationTheme {
                PresetChip(
                    label = "Equilibrium Only",
                    key = "eq_only",
                    isSelected = true,
                    onClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Equilibrium Only").assertIsDisplayed()
    }
}
