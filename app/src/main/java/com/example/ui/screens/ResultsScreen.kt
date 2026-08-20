package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ChemCard
import com.example.ui.components.ChemPrimaryButton
import com.example.ui.components.ChemSecondaryButton
import com.example.ui.theme.ChemCyan
import com.example.ui.theme.ChemGreen
import com.example.ui.theme.ChemOrange
import com.example.ui.theme.ChemPurple
import com.example.ui.theme.ChemYellow
import com.example.ui.viewmodels.ChemViewModel
import com.example.ui.viewmodels.ScreenState

@Composable
fun ResultsScreen(
    viewModel: ChemViewModel,
    modifier: Modifier = Modifier
) {
    val activeQuestions by viewModel.activeQuestions.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val matchHistory by viewModel.matchHistory.collectAsState()
    val weakestTopics by viewModel.weakestTopics.collectAsState()
    val selectedGameMode by viewModel.selectedGameMode.collectAsState()

    val latestMatch = matchHistory.firstOrNull()
    val totalQ = latestMatch?.totalQuestions ?: activeQuestions.size.coerceAtLeast(1)
    val correctQ = latestMatch?.correctCount ?: 0
    val accuracyPercent = (correctQ * 100) / totalQ

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 550.dp)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Learning Session Summary",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = ChemCyan,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Active Retrieval & Chemical Analysis Report",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Accuracy & Mastery Score Card
            item {
                ChemCard(borderColor = if (accuracyPercent >= 75) ChemGreen else ChemCyan) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = (if (accuracyPercent >= 75) ChemGreen else ChemCyan).copy(alpha = 0.18f),
                            modifier = Modifier.size(72.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = if (accuracyPercent >= 80) "🏆" else if (accuracyPercent >= 50) "🧠" else "💡",
                                    fontSize = 36.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$accuracyPercent% Accuracy",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = if (accuracyPercent >= 75) ChemGreen else ChemCyan
                        )
                        Text(
                            text = "$correctQ of $totalQ concepts mastered this session",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        LinearProgressIndicator(
                            progress = { accuracyPercent / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = if (accuracyPercent >= 75) ChemGreen else ChemCyan,
                            trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                    }
                }
            }

            // Streak & XP Card
            item {
                ChemCard(borderColor = ChemOrange.copy(alpha = 0.4f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = ChemOrange,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "${userProfile?.currentStreak ?: 1} Day Study Streak Active",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ChemOrange
                                )
                                Text(
                                    text = "Daily QCAA retrieval practice logged",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Weak Areas Diagnostic Notice
            if (weakestTopics.isNotEmpty()) {
                item {
                    ChemCard(borderColor = ChemOrange.copy(alpha = 0.5f)) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CrisisAlert,
                                    contentDescription = null,
                                    tint = ChemOrange,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Target Area for Next Session",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ChemOrange
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Your accuracy in ${weakestTopics.first()} can be improved. Consider launching a targeted session.",
                                fontSize = 11.sp,
                                lineHeight = 16.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            ChemPrimaryButton(
                                text = "🎯 Practice Weak Spots Now",
                                onClick = { viewModel.startWeakSpotsPractice() },
                                accentColor = ChemOrange,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            // Action Buttons
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    ChemPrimaryButton(
                        text = when (selectedGameMode) {
                            "retrieval" -> "Start Another Retrieval Round ↺"
                            "analysis" -> "Start Another Analysis Problem ↺"
                            else -> "Practice Again ↺"
                        },
                        onClick = {
                            when (selectedGameMode) {
                                "retrieval" -> viewModel.startRetrievalPractice()
                                "analysis" -> viewModel.startDeepAnalysisMode()
                                "weak_spots" -> viewModel.startWeakSpotsPractice()
                                else -> viewModel.startPracticeMode()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "results_repeat_btn"
                    )

                    ChemSecondaryButton(
                        text = "📊 View Diagnostic Center & Achievements",
                        onClick = { viewModel.navigateTo(ScreenState.ANALYTICS) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    ChemSecondaryButton(
                        text = "← Return to Home",
                        onClick = { viewModel.navigateTo(ScreenState.HOME) },
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "results_home_btn"
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
