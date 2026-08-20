package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
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
import androidx.compose.ui.text.style.TextAlign
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
import com.example.ui.theme.ChemPurple
import com.example.ui.theme.ChemRed
import com.example.ui.theme.ChemYellow
import com.example.ui.viewmodels.ChemViewModel
import com.example.ui.viewmodels.ScreenState

@Composable
fun RetrievalScreen(
    viewModel: ChemViewModel,
    modifier: Modifier = Modifier
) {
    val activeQuestions by viewModel.activeQuestions.collectAsState()
    val currentQIndex by viewModel.currentQIndex.collectAsState()
    val isAnswered by viewModel.isAnswered.collectAsState()
    val isRetrievalRevealed by viewModel.isRetrievalRevealed.collectAsState()
    val selectedOptionIndex by viewModel.selectedOptionIndex.collectAsState()
    val feedbackMessage by viewModel.feedbackMessage.collectAsState()
    val isFeedbackCorrect by viewModel.isFeedbackCorrect.collectAsState()

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
                                contentDescription = "Exit Retrieval",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Active Retrieval · Q ${currentQIndex + 1}/${activeQuestions.size}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = ChemCyan
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = ChemCyan.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "💡 ACTIVE RECALL",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = ChemCyan,
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
                        Spacer(modifier = Modifier.height(4.dp))
                        // Topic & Subtopic Badges
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = ChemCyan.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "${currentQuestion.topic.uppercase()} · ${currentQuestion.subtopic}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ChemCyan,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
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
                        ChemCard(borderColor = ChemCyan.copy(alpha = 0.5f)) {
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

                    // Active Retrieval Memory Prompt (Before Revealing Options)
                    if (!isRetrievalRevealed && !isAnswered) {
                        item {
                            ChemCard(borderColor = ChemOrange) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Psychology,
                                        contentDescription = null,
                                        tint = ChemOrange,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "PAUSE & RETRIEVE FROM MEMORY",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.sp,
                                        color = ChemOrange
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = if (currentQuestion.retrievalCue.isNotBlank()) currentQuestion.retrievalCue else "Before looking at the answer choices, try to recall the underlying chemical principle or formula in your mind.",
                                        fontSize = 13.sp,
                                        textAlign = TextAlign.Center,
                                        lineHeight = 18.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    ChemPrimaryButton(
                                        text = "Reveal Options & Check Answer →",
                                        onClick = { viewModel.revealRetrievalOptions() },
                                        modifier = Modifier.fillMaxWidth(),
                                        testTag = "reveal_retrieval_btn"
                                    )
                                }
                            }
                        }
                    }

                    // Options Grid (Revealed after Active Retrieval Pause)
                    if (isRetrievalRevealed || isAnswered) {
                        if (currentQuestion.type == "mcq") {
                            item {
                                val letters = listOf("A", "B", "C", "D")
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    currentQuestion.options.forEachIndexed { index, optionText ->
                                        val isSelected = selectedOptionIndex == index
                                        val isCorrect = index == currentQuestion.answerIndex

                                        val optionBorder = when {
                                            isAnswered && isCorrect -> ChemGreen
                                            isAnswered && isSelected && !isCorrect -> ChemRed
                                            isSelected -> ChemCyan
                                            else -> MaterialTheme.colorScheme.outline
                                        }

                                        val optionBg = when {
                                            isAnswered && isCorrect -> ChemGreen.copy(alpha = 0.15f)
                                            isAnswered && isSelected && !isCorrect -> ChemRed.copy(alpha = 0.15f)
                                            isSelected -> ChemCyan.copy(alpha = 0.15f)
                                            else -> MaterialTheme.colorScheme.surfaceVariant
                                        }

                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = optionBg,
                                            border = BorderStroke(if (isSelected || (isAnswered && isCorrect)) 2.dp else 1.dp, optionBorder),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable(enabled = !isAnswered) {
                                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                    viewModel.submitRetrievalOption(index)
                                                }
                                                .testTag("retrieval_opt_$index")
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
                                            text = "Direct Answer: ${currentQuestion.answerShort}",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ChemCyan
                                        )
                                        if (!isAnswered) {
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(
                                                text = "Did you recall this correctly?",
                                                fontSize = 12.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                ChemPrimaryButton(
                                                    text = "Yes, Got It! 👍",
                                                    onClick = { viewModel.submitRetrievalSelfEvaluation(true) },
                                                    accentColor = ChemGreen,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                ChemSecondaryButton(
                                                    text = "Need Review 🔄",
                                                    onClick = { viewModel.submitRetrievalSelfEvaluation(false) },
                                                    modifier = Modifier.weight(1f)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Explanation Feedback
                    if (isAnswered && feedbackMessage.isNotBlank()) {
                        item {
                            FeedbackCard(
                                isCorrect = isFeedbackCorrect,
                                message = if (isFeedbackCorrect) "Concept Verified!" else "Chemistry Principle Breakdown",
                                explanation = feedbackMessage
                            )
                        }
                    }

                    // Next Question Action
                    if (isAnswered) {
                        item {
                            ChemPrimaryButton(
                                text = if (currentQIndex + 1 >= activeQuestions.size) "COMPLETE RETRIEVAL PRACTICE →" else "NEXT RETRIEVAL QUESTION →",
                                onClick = { viewModel.nextRetrievalQuestion() },
                                modifier = Modifier.fillMaxWidth(),
                                testTag = "next_retrieval_btn"
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }
            }
        }
    }
}
