package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.Question
import com.example.ui.components.ChemCard
import com.example.ui.components.ChemPrimaryButton
import com.example.ui.components.FeedbackCard
import com.example.ui.components.ScoreboardRow
import com.example.ui.components.TacticalBuzzerButton
import com.example.ui.components.TimerProgressBar
import com.example.ui.theme.ChemCyan
import com.example.ui.theme.ChemGreen
import com.example.ui.theme.ChemOrange
import com.example.ui.theme.ChemPurple
import com.example.ui.theme.ChemRed
import com.example.ui.theme.ChemYellow
import com.example.ui.viewmodels.ChemViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GamePlayScreen(
    viewModel: ChemViewModel,
    modifier: Modifier = Modifier
) {
    val activeQuestions by viewModel.activeQuestions.collectAsState()
    val currentQIndex by viewModel.currentQIndex.collectAsState()
    val players by viewModel.players.collectAsState()
    val myPlayerId by viewModel.myPlayerId.collectAsState()
    val gameMode by viewModel.selectedGameMode.collectAsState()
    val isAnswered by viewModel.isAnswered.collectAsState()
    val isReadingPhase by viewModel.isReadingPhase.collectAsState()
    val selectedOptionIndex by viewModel.selectedOptionIndex.collectAsState()
    val feedbackMessage by viewModel.feedbackMessage.collectAsState()
    val isFeedbackCorrect by viewModel.isFeedbackCorrect.collectAsState()
    val buzzedPlayerName by viewModel.buzzedPlayerName.collectAsState()
    val buzzedPlayerId by viewModel.buzzedPlayerId.collectAsState()
    val timerSeconds by viewModel.timerSeconds.collectAsState()
    val maxTimerSeconds by viewModel.maxTimerSeconds.collectAsState()

    var showExitDialog by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    val currentQuestion: Question? = activeQuestions.getOrNull(currentQIndex)
    val isBuzzerMode = gameMode == "blitz"
    val turnPlayer = viewModel.getCurrentTurnPlayer()
    val isMyTurn = (turnPlayer?.id == myPlayerId) || isBuzzerMode

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Exit Battle?", fontWeight = FontWeight.Bold) },
            text = { Text("Your match progress and current streak points will be forfeited.") },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    viewModel.leaveMatch()
                }) {
                    Text("Exit", color = ChemRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Stay")
                }
            }
        )
    }

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
                            onClick = { showExitDialog = true },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Exit Battle",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Q ${currentQIndex + 1} / ${activeQuestions.size}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = ChemCyan
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = if (isBuzzerMode) "🔔 BUZZER BLITZ" else "🎮 QUIZ BATTLE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = if (isBuzzerMode) ChemOrange else ChemPurple,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Scoreboard Bar
            ScoreboardRow(
                players = players,
                myId = myPlayerId,
                modifier = Modifier.padding(top = 4.dp)
            )

            // Timer Progress Bar
            if (!isReadingPhase) {
                TimerProgressBar(
                    timeLeft = timerSeconds,
                    totalSeconds = maxTimerSeconds,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            // Scrollable Main Question Area
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
                        // Topic Badge & Bookmark
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = ChemCyan.copy(alpha = 0.15f),
                                border = BorderStroke(1.dp, ChemCyan.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = "📍 ${currentQuestion.topic.uppercase()}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp,
                                    color = ChemCyan,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }

                            IconButton(
                                onClick = { viewModel.toggleBookmark(currentQuestion) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.BookmarkBorder,
                                    contentDescription = "Bookmark Question",
                                    tint = ChemYellow,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    // Turn Label (for non-buzzer modes)
                    if (!isBuzzerMode) {
                        item {
                            Text(
                                text = if (isMyTurn) "👉 Your Turn to Answer!" else "⏳ Waiting for ${turnPlayer?.name ?: "Opponent"}...",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isMyTurn) ChemCyan else ChemOrange
                            )
                        }
                    }

                    // Question Card
                    item {
                        ChemCard(
                            borderColor = if (isMyTurn) ChemCyan.copy(alpha = 0.4f) else MaterialTheme.colorScheme.outline
                        ) {
                            Column {
                                if (currentQuestion.isPastExam) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = ChemYellow.copy(alpha = 0.2f),
                                        modifier = Modifier.padding(bottom = 8.dp)
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

                    // Reading Phase UI (Quiz Battle)
                    if (isReadingPhase && isMyTurn) {
                        item {
                            ChemCard(borderColor = ChemYellow) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "📖 READING STAGE",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 1.sp,
                                        color = ChemYellow
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Read the question carefully. When you are ready to see the answer options, press below.",
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(14.dp))
                                    ChemPrimaryButton(
                                        text = "I'm Ready →",
                                        onClick = { viewModel.revealOptionsAndStartTimer() },
                                        modifier = Modifier.widthIn(max = 220.dp),
                                        testTag = "reading_ready_button"
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Auto-starts in ${timerSeconds}s",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    // Buzzer Button (Speed Round)
                    if (isBuzzerMode && !isAnswered) {
                        item {
                            TacticalBuzzerButton(
                                enabled = buzzedPlayerId == null,
                                buzzedPlayerName = buzzedPlayerName,
                                isMyBuzz = buzzedPlayerId == myPlayerId,
                                onBuzz = { viewModel.onBuzzIn() }
                            )
                        }
                    }

                    // MCQ Options Grid (if not reading phase)
                    if (!isReadingPhase && currentQuestion.type == "mcq") {
                        item {
                            val isClickable = !isAnswered && (if (isBuzzerMode) buzzedPlayerId == myPlayerId else isMyTurn)
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
                                            .clickable(enabled = isClickable) {
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                viewModel.submitOption(index)
                                            }
                                            .testTag("option_$index")
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
                                                        fontWeight = FontWeight.ExtraBold,
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
                    }

                    // Short Answer Field (if not reading phase)
                    if (!isReadingPhase && currentQuestion.type == "short") {
                        item {
                            var shortText by remember { mutableStateOf("") }
                            val isClickable = !isAnswered && isMyTurn

                            ChemCard {
                                Column {
                                    OutlinedTextField(
                                        value = shortText,
                                        onValueChange = {
                                            shortText = it
                                            (viewModel.shortAnswerInput as? kotlinx.coroutines.flow.MutableStateFlow)?.value = it
                                        },
                                        placeholder = { Text("Type chemistry term or formula...") },
                                        enabled = isClickable,
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                        keyboardActions = KeyboardActions(onDone = {
                                            if (shortText.isNotBlank()) {
                                                viewModel.submitShortAnswer()
                                            }
                                        }),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("short_answer_input"),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = ChemCyan,
                                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                        ),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    ChemPrimaryButton(
                                        text = "SUBMIT ANSWER",
                                        onClick = {
                                            (viewModel.shortAnswerInput as? kotlinx.coroutines.flow.MutableStateFlow)?.value = shortText
                                            viewModel.submitShortAnswer()
                                        },
                                        enabled = isClickable && shortText.isNotBlank(),
                                        modifier = Modifier.fillMaxWidth(),
                                        testTag = "submit_short_answer_btn"
                                    )
                                }
                            }
                        }
                    }

                    // Feedback Box
                    if (isAnswered && feedbackMessage.isNotBlank()) {
                        item {
                            FeedbackCard(
                                isCorrect = isFeedbackCorrect,
                                message = if (isFeedbackCorrect) "Correct!" else "Explanation",
                                explanation = feedbackMessage
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
