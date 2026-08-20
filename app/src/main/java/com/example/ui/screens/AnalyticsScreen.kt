package com.example.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.AchievementBadge
import com.example.data.models.QuestionsData
import com.example.data.models.SubtopicMastery
import com.example.ui.components.ChemCard
import com.example.ui.components.ChemPrimaryButton
import com.example.ui.components.ChemSecondaryButton
import com.example.ui.theme.ChemCyan
import com.example.ui.theme.ChemGreen
import com.example.ui.theme.ChemOrange
import com.example.ui.theme.ChemPurple
import com.example.ui.theme.ChemRed
import com.example.ui.theme.ChemYellow
import com.example.ui.viewmodels.ChemViewModel
import com.example.ui.viewmodels.ScreenState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AnalyticsScreen(
    viewModel: ChemViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val topicStats by viewModel.topicStats.collectAsState()
    val subtopicMasteryList by viewModel.subtopicMasteryList.collectAsState()
    val weakestTopics by viewModel.weakestTopics.collectAsState()
    val matchHistory by viewModel.matchHistory.collectAsState()
    val achievementBadges by viewModel.achievementBadges.collectAsState()

    val totalAttempts = topicStats.sumOf { it.questionsAttempted }
    val totalCorrect = topicStats.sumOf { it.questionsCorrect }
    val overallAccuracy = if (totalAttempts > 0) (totalCorrect * 100) / totalAttempts else 0

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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { viewModel.navigateTo(ScreenState.HOME) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "Student Achievement & Weakness Diagnostic",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(2.dp))
                }

                // Summary Stats Grid
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatCard(
                            label = "Study Streak",
                            value = "${userProfile?.currentStreak ?: 1} Days",
                            color = ChemOrange,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = "Overall Accuracy",
                            value = if (totalAttempts > 0) "$overallAccuracy%" else "--",
                            color = ChemCyan,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = "Concepts Practiced",
                            value = "$totalAttempts",
                            color = ChemGreen,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // 1. WEAKEST AREAS & DIAGNOSTIC ACTION SECTION
                item {
                    Text(
                        text = "DIAGNOSTIC WEAK-AREA ANALYSIS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = ChemOrange
                    )
                }

                item {
                    ChemCard(borderColor = ChemOrange.copy(alpha = 0.6f)) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CrisisAlert,
                                        contentDescription = null,
                                        tint = ChemOrange,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Identified Learning Gaps",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            if (weakestTopics.isNotEmpty()) {
                                Text(
                                    text = "Based on your active recall and problem-solving attempts, these subtopics need reinforcement before your exam:",
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    weakestTopics.forEach { topicName ->
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = ChemRed.copy(alpha = 0.12f),
                                            border = BorderStroke(1.dp, ChemRed.copy(alpha = 0.3f)),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Warning,
                                                    contentDescription = null,
                                                    tint = ChemRed,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = topicName,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                ChemPrimaryButton(
                                    text = "🎯 Launch Targeted Weak-Spots Practice",
                                    onClick = { viewModel.startWeakSpotsPractice() },
                                    accentColor = ChemOrange,
                                    modifier = Modifier.fillMaxWidth(),
                                    testTag = "fix_weak_spots_btn"
                                )
                            } else {
                                Text(
                                    text = "No critical weak areas detected! Complete more Active Retrieval and Deep Analysis sessions to generate detailed diagnostic reports.",
                                    fontSize = 12.sp,
                                    color = ChemGreen,
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }
                }

                // 2. SUBTOPIC MASTERY BREAKDOWN
                item {
                    Text(
                        text = "QCAA CURRICULUM TOPIC MASTERY",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                item {
                    ChemCard {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            QuestionsData.TOPICS.forEach { topicName ->
                                val stat = topicStats.firstOrNull { it.topicName == topicName }
                                val attempted = stat?.questionsAttempted ?: 0
                                val correct = stat?.questionsCorrect ?: 0
                                val accuracy = if (attempted > 0) (correct * 100) / attempted else 0

                                val progressColor = when {
                                    accuracy >= 80 -> ChemGreen
                                    accuracy >= 60 -> ChemCyan
                                    attempted > 0 -> ChemRed
                                    else -> MaterialTheme.colorScheme.outline
                                }

                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = topicName,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = if (attempted > 0) "$accuracy% ($correct/$attempted)" else "Not Practiced",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = progressColor
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    LinearProgressIndicator(
                                        progress = { if (attempted > 0) (accuracy / 100f) else 0f },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(6.dp)
                                            .clip(RoundedCornerShape(3.dp)),
                                        color = progressColor,
                                        trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                                    )
                                }
                            }
                        }
                    }
                }

                // 3. STUDENT ACHIEVEMENT BADGES
                item {
                    Text(
                        text = "STUDENT ACHIEVEMENTS & MILESTONES",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                item {
                    ChemCard {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            achievementBadges.forEach { badge ->
                                AchievementRow(badge = badge)
                            }
                        }
                    }
                }

                // 4. RECENT PRACTICE SESSIONS LOG
                item {
                    Text(
                        text = "RECENT RETRIEVAL & ANALYSIS SESSIONS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (matchHistory.isEmpty()) {
                    item {
                        ChemCard {
                            Text(
                                text = "No sessions logged yet. Complete a learning session to track your growth over time.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(matchHistory.take(8).size) { index ->
                        val match = matchHistory[index]
                        val dateStr = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(Date(match.timestamp))
                        val modeLabel = when (match.gameMode) {
                            "retrieval" -> "💡 Active Retrieval"
                            "analysis" -> "🧠 Deep Analysis"
                            "weak_spots" -> "🎯 Weak Spots Fix"
                            else -> "🔬 Lab Practice"
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = modeLabel,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ChemCyan
                                    )
                                    Text(
                                        text = "$dateStr · ${match.focusTopic}",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (match.correctCount * 2 >= match.totalQuestions) ChemGreen.copy(alpha = 0.2f) else ChemOrange.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "${match.correctCount}/${match.totalQuestions} Correct",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (match.correctCount * 2 >= match.totalQuestions) ChemGreen else ChemOrange,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun AchievementRow(badge: AchievementBadge) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (badge.isUnlocked) ChemCyan.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, if (badge.isUnlocked) ChemCyan else MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = if (badge.isUnlocked) ChemCyan.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = badge.icon, fontSize = 18.sp)
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = badge.title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (badge.isUnlocked) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Unlocked",
                            tint = ChemGreen,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
                Text(
                    text = badge.description,
                    fontSize = 10.sp,
                    lineHeight = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (!badge.isUnlocked && badge.progress > 0f) {
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { badge.progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = ChemCyan,
                        trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    )
                }
            }
        }
    }
}

@Composable
fun StatCard(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = color
            )
            Text(
                text = label,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
