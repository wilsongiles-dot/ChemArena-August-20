package com.example

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import com.example.ui.components.ChemCard
import com.example.ui.components.ChemPrimaryButton
import com.example.ui.screens.LearningModeCard
import com.example.ui.theme.ChemCyan
import com.example.ui.theme.MyApplicationTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun learning_card_light_screenshot() {
        composeTestRule.setContent {
            MyApplicationTheme(darkTheme = false) {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
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
        }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/learning_card_light.png")
    }

    @Test
    fun learning_card_dark_screenshot() {
        composeTestRule.setContent {
            MyApplicationTheme(darkTheme = true) {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    LearningModeCard(
                        title = "🧠 Deep Chemistry Analysis",
                        subtitle = "Step-by-step cognitive scaffolding on multi-step QCAA chemical scenarios.",
                        badgeText = "QCAA PROBLEM SOLVING",
                        badgeColor = ChemCyan,
                        accentColor = ChemCyan,
                        icon = Icons.Default.Psychology,
                        onClick = {},
                        testTag = "mode_analysis"
                    )
                }
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/learning_card_dark.png")
    }
}
