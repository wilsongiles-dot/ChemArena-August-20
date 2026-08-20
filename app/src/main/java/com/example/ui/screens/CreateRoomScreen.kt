package com.example.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.QuestionsData
import com.example.ui.components.ChemCard
import com.example.ui.components.ChemPrimaryButton
import com.example.ui.components.ChemSecondaryButton
import com.example.ui.components.TopicChip
import com.example.ui.theme.ChemCyan
import com.example.ui.theme.ChemOrange
import com.example.ui.theme.ChemPurple
import com.example.ui.viewmodels.ChemViewModel
import com.example.ui.viewmodels.ScreenState

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CreateRoomScreen(
    viewModel: ChemViewModel,
    modifier: Modifier = Modifier
) {
    val playerName by viewModel.playerNameInput.collectAsState()
    val selectedMode by viewModel.selectedGameMode.collectAsState()
    val selectedQCount by viewModel.selectedQuestionCount.collectAsState()
    val selectedTopics by viewModel.selectedTopics.collectAsState()
    val curriculumPreset by viewModel.curriculumPreset.collectAsState()

    val presets = listOf(
        "all" to "Full Course (All Topics)",
        "eq_only" to "Unit 3: Equilibrium Only",
        "eq_acid" to "Unit 3: Equilibrium + Acids",
        "unit3" to "All Unit 3 (Eq, Acid, Redox)",
        "unit3_org" to "Unit 3 + Organic Chem",
        "unit3_org_synth" to "Unit 3 + 4 Synthesis"
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 600.dp)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
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
                        text = "Create Room",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Host Name
            item {
                ChemCard {
                    Column {
                        Text(
                            text = "👤 YOUR HOST NAME",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = playerName,
                            onValueChange = { viewModel.playerNameInput.value = it },
                            placeholder = { Text("Enter your name or gamer tag...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("host_name_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ChemCyan,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            ),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )
                    }
                }
            }

            // Game Mode Selection
            item {
                ChemCard {
                    Column {
                        Text(
                            text = "🎮 GAME MODE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ModeSelectButton(
                                title = "Quiz Battle",
                                icon = "🧠",
                                isSelected = selectedMode == "quiz",
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.selectedGameMode.value = "quiz" }
                            )
                            ModeSelectButton(
                                title = "Buzzer Round",
                                icon = "🔔",
                                isSelected = selectedMode == "blitz",
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.selectedGameMode.value = "blitz" }
                            )
                        }
                    }
                }
            }

            // Question Count
            item {
                ChemCard {
                    Column {
                        Text(
                            text = "🔢 QUESTIONS PER ROUND",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(5, 10, 15, 20).forEach { count ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (selectedQCount == count) ChemCyan.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.5.dp,
                                        if (selectedQCount == count) ChemCyan else MaterialTheme.colorScheme.outline
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { viewModel.selectedQuestionCount.value = count }
                                ) {
                                    Text(
                                        text = "$count",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selectedQCount == count) ChemCyan else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(vertical = 10.dp),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Curriculum Progress Presets
            item {
                ChemCard {
                    Column {
                        Text(
                            text = "📍 CURRICULUM PROGRESS (WHAT WAS COVERED?)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            presets.forEach { (key, label) ->
                                val isSelected = curriculumPreset == key
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (isSelected) ChemOrange.copy(alpha = 0.18f) else MaterialTheme.colorScheme.surface,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.2.dp,
                                        if (isSelected) ChemOrange else MaterialTheme.colorScheme.outline
                                    ),
                                    modifier = Modifier.clickable { viewModel.setCurriculumFilter(key) }
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) ChemOrange else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "OR TOGGLE TOPICS MANUALLY:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            QuestionsData.TOPICS.forEach { topic ->
                                val isSelected = selectedTopics.contains(topic)
                                TopicChip(
                                    text = topic,
                                    isSelected = isSelected,
                                    onToggle = { viewModel.toggleTopic(topic) }
                                )
                            }
                        }
                    }
                }
            }

            // Action Buttons
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    ChemPrimaryButton(
                        text = "CREATE ONLINE ROOM →",
                        onClick = { viewModel.createOnlineRoom() },
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "create_room_submit_btn"
                    )
                    ChemSecondaryButton(
                        text = "Play Locally with Smart Bot",
                        onClick = { viewModel.startOfflineGame() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ModeSelectButton(
    title: String,
    icon: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) ChemCyan.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(
            1.5.dp,
            if (isSelected) ChemCyan else MaterialTheme.colorScheme.outline
        ),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = icon, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) ChemCyan else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
