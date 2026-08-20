package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.Question
import com.example.ui.components.ChemCard
import com.example.ui.components.ChemPrimaryButton
import com.example.ui.components.ChemSecondaryButton
import com.example.ui.components.FeedbackCard
import com.example.ui.theme.ChemCyan
import com.example.ui.theme.ChemGreen
import com.example.ui.theme.ChemOrange
import com.example.ui.theme.ChemRed
import com.example.ui.theme.ChemYellow
import com.example.ui.viewmodels.ChemViewModel
import com.example.ui.viewmodels.ScreenState

@Composable
fun PracticeScreen(
    viewModel: ChemViewModel,
    modifier: Modifier = Modifier
) {
    val activeQuestions by viewModel.activeQuestions.collectAsState()
    val currentQIndex by viewModel.currentQIndex.collectAsState()
    val isAnswered by viewModel.isAnswered.collectAsState()
    val selectedOptionIndex by viewModel.selectedOptionIndex.collectAsState()
    val feedbackMessage by viewModel.feedbackMessage.collectAsState()
    val isFeedbackCorrect by viewModel.isFeedbackCorrect.collectAsState()
    val practiceHintRevealed by viewModel.practiceHintRevealed.collectAsState()

    val currentQuestion: Question? = activeQuestions.getOrNull(currentQIndex)
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 650.dp)
        ) {
            // Header Bar
            Surface(
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { viewModel.navigateTo(ScreenState.HOME) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Exit Practice",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Lab Practice · Q ${currentQIndex + 1}/${activeQuestions.size}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = ChemGreen
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = ChemGreen.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "🔬 SELF-PACED",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = ChemGreen,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            // Scrollable Content
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (currentQuestion != null) {
                    item {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = ChemGreen.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "📍 ${currentQuestion.topic.uppercase()}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ChemGreen,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }

                            IconButton(
                                onClick = { viewModel.toggleBookmark(currentQuestion) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.BookmarkBorder,
                                    contentDescription = "Bookmark",
                                    tint = ChemYellow,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    // Question Card
                    item {
                        ChemCard(borderColor = ChemGreen.copy(alpha = 0.5f)) {
                            Column {
                                if (currentQuestion.isPastExam) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = ChemYellow.copy(alpha = 0.2f),
                                        modifier = Modifier.padding(bottom = 6.dp)
                                    ) {
                                        Text(
                                            text = "★ QCAA ${currentQuestion.examYear} EXAM",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ChemYellow,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = currentQuestion.q,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 23.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    // Options Grid
                    if (currentQuestion.type == "mcq") {
                        item {
                            val letters = listOf("A", "B", "C", "D")
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                currentQuestion.options.forEachIndexed { index, optionText ->
                                    val isSelected = selectedOptionIndex == index
                                    val isCorrect = index == currentQuestion.answerIndex

                                    val optionBorder = when {
                                        isAnswered && isCorrect -> ChemGreen
                                        isSelected && !isFeedbackCorrect -> ChemRed
                                        isSelected -> ChemCyan
                                        else -> MaterialTheme.colorScheme.outline
                                    }

                                    val optionBg = when {
                                        isAnswered && isCorrect -> ChemGreen.copy(alpha = 0.15f)
                                        isSelected && !isFeedbackCorrect -> ChemRed.copy(alpha = 0.15f)
                                        isSelected -> ChemCyan.copy(alpha = 0.15f)
                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = optionBg,
                                        border = BorderStroke(if (isSelected || (isAnswered && isCorrect)) 2.dp else 1.dp, optionBorder),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                viewModel.submitPracticeOption(index)
                                            }
                                            .testTag("practice_opt_$index")
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = if (isSelected) ChemCyan else MaterialTheme.colorScheme.surface,
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Text(
                                                        text = letters.getOrElse(index) { "${index + 1}" },
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (isSelected) androidx.compose.ui.graphics.Color.Black else MaterialTheme.colorScheme.onSurface
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(
                                                text = optionText,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Medium,
                                                lineHeight = 18.sp,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        item {
                            ChemCard {
                                Column {
                                    Text(
                                        text = "Answer: ${currentQuestion.answerShort}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ChemCyan
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = currentQuestion.explanation,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    // Hint / Explanation Card
                    if (feedbackMessage.isNotBlank()) {
                        item {
                            FeedbackCard(
                                isCorrect = isFeedbackCorrect,
                                message = if (isFeedbackCorrect) "Correct Answer!" else "Guidance / Hint",
                                explanation = feedbackMessage
                            )
                        }
                    }

                    // Next Question Button
                    item {
                        ChemPrimaryButton(
                            text = if (currentQIndex + 1 >= activeQuestions.size) "FINISH PRACTICE →" else "NEXT QUESTION →",
                            onClick = { viewModel.nextPracticeQuestion() },
                            accentColor = ChemGreen,
                            modifier = Modifier.fillMaxWidth(),
                            testTag = "practice_next_btn"
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}
