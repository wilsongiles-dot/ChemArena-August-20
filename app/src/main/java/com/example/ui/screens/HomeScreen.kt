package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.QuestionsData
import com.example.ui.components.ChemCard
import com.example.ui.components.ChemPrimaryButton
import com.example.ui.theme.ChemCyan
import com.example.ui.theme.ChemGreen
import com.example.ui.theme.ChemOrange
import com.example.ui.theme.ChemPurple
import com.example.ui.theme.ChemRed
import com.example.ui.theme.ChemYellow
import com.example.ui.viewmodels.ChemViewModel
import com.example.ui.viewmodels.ScreenState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    viewModel: ChemViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val topicStats by viewModel.topicStats.collectAsState()
    val weakestTopics by viewModel.weakestTopics.collectAsState()
    val selectedTopics by viewModel.selectedTopics.collectAsState()
    val curriculumPreset by viewModel.curriculumPreset.collectAsState()

    // Calculate Overall QCAA Readiness Mastery
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
            // App Bar Header
            Surface(
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = ChemCyan.copy(alpha = 0.2f),
                            border = BorderStroke(1.5.dp, ChemCyan),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = userProfile?.avatar ?: "🧪",
                                    fontSize = 18.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "ChemArena",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "QCAA Chemistry Units 3 & 4",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Study Streak Badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = ChemOrange.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, ChemOrange.copy(alpha = 0.4f)),
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = "Streak",
                                    tint = ChemOrange,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "${userProfile?.currentStreak ?: 1}d",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ChemOrange
                                )
                            }
                        }

                        IconButton(
                            onClick = { viewModel.navigateTo(ScreenState.ANALYTICS) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoGraph,
                                contentDescription = "Analytics & Achievements",
                                tint = ChemCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = { viewModel.navigateTo(ScreenState.SETTINGS) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Main Scrollable Area
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(2.dp))
                }

                // Student Achievement & Diagnostic Quick Overview Card
                item {
                    ChemCard(borderColor = ChemCyan.copy(alpha = 0.4f)) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "STUDENT MASTERY & READINESS",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp,
                                        color = ChemCyan
                                    )
                                    Text(
                                        text = if (totalAttempts > 0) "$overallAccuracy% Syllabus Accuracy" else "Begin Retrieval to Track Mastery",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (overallAccuracy >= 80) ChemGreen.copy(alpha = 0.2f) else ChemCyan.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = if (overallAccuracy >= 80) "READY ★" else "IN PROGRESS",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (overallAccuracy >= 80) ChemGreen else ChemCyan,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            LinearProgressIndicator(
                                progress = { (overallAccuracy / 100f).coerceIn(0.05f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = ChemCyan,
                                trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )

                            // Weak Areas Callout
                            if (weakestTopics.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = ChemOrange.copy(alpha = 0.12f),
                                    border = BorderStroke(1.dp, ChemOrange.copy(alpha = 0.3f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.CrisisAlert,
                                                contentDescription = null,
                                                tint = ChemOrange,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column {
                                                Text(
                                                    text = "NEEDS FOCUS (WEAKEST AREA)",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = ChemOrange
                                                )
                                                Text(
                                                    text = weakestTopics.first(),
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }

                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = ChemOrange,
                                            modifier = Modifier.clickable { viewModel.startWeakSpotsPractice() }
                                        ) {
                                            Text(
                                                text = "Fix Now →",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Curriculum Filter Presets
                item {
                    Text(
                        text = "FOCUS TOPIC PRESET",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        PresetChip("All Curriculum", "all", curriculumPreset == "all") { viewModel.setCurriculumFilter("all") }
                        PresetChip("Equilibrium Only", "eq_only", curriculumPreset == "eq_only") { viewModel.setCurriculumFilter("eq_only") }
                        PresetChip("Eq + Acids", "eq_acid", curriculumPreset == "eq_acid") { viewModel.setCurriculumFilter("eq_acid") }
                        PresetChip("Unit 3 Focus", "unit3", curriculumPreset == "unit3") { viewModel.setCurriculumFilter("unit3") }
                        PresetChip("Unit 3 + Organic", "unit3_org", curriculumPreset == "unit3_org") { viewModel.setCurriculumFilter("unit3_org") }
                    }
                }

                // Main Core Learning Modes
                item {
                    Text(
                        text = "CORE CHEMISTRY LEARNING MODES",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // 1. Active Retrieval Practice
                item {
                    LearningModeCard(
                        title = "💡 Active Retrieval Practice",
                        subtitle = "Recall core principles before seeing options. Strengthen long-term memory without speed pressure.",
                        badgeText = "RECOMMENDED",
                        badgeColor = ChemCyan,
                        accentColor = ChemCyan,
                        icon = Icons.Default.Psychology,
                        onClick = { viewModel.startRetrievalPractice() },
                        testTag = "mode_retrieval"
                    )
                }

                // 2. Deep Chemistry Analysis
                item {
                    LearningModeCard(
                        title = "🧠 Deep Chemistry Analysis",
                        subtitle = "Step-by-step cognitive scaffolding on multi-step QCAA chemical scenarios, equilibria, and redox.",
                        badgeText = "QCAA PROBLEM SOLVING",
                        badgeColor = ChemPurple,
                        accentColor = ChemPurple,
                        icon = Icons.Default.Science,
                        onClick = { viewModel.startDeepAnalysisMode() },
                        testTag = "mode_analysis"
                    )
                }

                // 3. Targeted Weak-Spots Fix
                item {
                    LearningModeCard(
                        title = "🎯 Target Weakest Concepts",
                        subtitle = "Personalized practice automatically generated from your missed questions and lowest scoring subtopics.",
                        badgeText = "DIAGNOSTIC",
                        badgeColor = ChemOrange,
                        accentColor = ChemOrange,
                        icon = Icons.Default.TrackChanges,
                        onClick = { viewModel.startWeakSpotsPractice() },
                        testTag = "mode_weak_spots"
                    )
                }

                // 4. Untimed Lab Practice
                item {
                    LearningModeCard(
                        title = "🔬 Untimed Lab Practice",
                        subtitle = "Freely practice the entire question bank with instant hints, formulas, and step-by-step explanations.",
                        badgeText = "SELF-PACED",
                        badgeColor = ChemGreen,
                        accentColor = ChemGreen,
                        icon = Icons.Default.MenuBook,
                        onClick = { viewModel.startPracticeMode() },
                        testTag = "mode_practice"
                    )
                }

                // Secondary Navigation: Question Bank & Achievements
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.navigateTo(ScreenState.QUESTION_BANK) }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = null,
                                    tint = ChemCyan,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Question Archive",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Browse all QCAA Qs",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.navigateTo(ScreenState.ANALYTICS) }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoGraph,
                                    contentDescription = null,
                                    tint = ChemYellow,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Weakness Diagnostic",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Track subtopic gaps",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
fun LearningModeCard(
    title: String,
    subtitle: String,
    badgeText: String,
    badgeColor: Color,
    accentColor: Color,
    icon: ImageVector,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.2.dp, accentColor.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = accentColor.copy(alpha = 0.18f),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = badgeColor.copy(alpha = 0.18f)
                ) {
                    Text(
                        text = badgeText,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp,
                        color = badgeColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = subtitle,
                fontSize = 11.sp,
                lineHeight = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun PresetChip(
    label: String,
    key: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) ChemCyan.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, if (isSelected) ChemCyan else MaterialTheme.colorScheme.outline),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) ChemCyan else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}
