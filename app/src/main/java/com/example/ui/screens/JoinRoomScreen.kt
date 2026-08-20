package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.QuestionsData
import com.example.ui.components.ChemCard
import com.example.ui.components.ChemPrimaryButton
import com.example.ui.components.ChemSecondaryButton
import com.example.ui.theme.ChemCyan
import com.example.ui.theme.ChemOrange
import com.example.ui.viewmodels.ChemViewModel
import com.example.ui.viewmodels.ScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinRoomScreen(
    viewModel: ChemViewModel,
    modifier: Modifier = Modifier
) {
    val playerName by viewModel.playerNameInput.collectAsState()
    val roomCode by viewModel.joinRoomCodeInput.collectAsState()
    val selectedAvatar by viewModel.selectedAvatar.collectAsState()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 500.dp)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { viewModel.navigateTo(ScreenState.HOME) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "Join Room",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Room Code Input (Large stylized 4-digit field)
            item {
                ChemCard(borderColor = ChemCyan.copy(alpha = 0.6f)) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🔑 4-DIGIT ROOM CODE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = roomCode,
                            onValueChange = {
                                if (it.length <= 4) {
                                    viewModel.joinRoomCodeInput.value = it.uppercase()
                                }
                            },
                            placeholder = {
                                Text(
                                    "----",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            textStyle = TextStyle(
                                fontSize = 32.sp,
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = TextAlign.Center,
                                letterSpacing = 8.sp,
                                color = ChemCyan
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("join_room_code_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ChemCyan,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                }
            }

            // Your Name & Avatar
            item {
                ChemCard {
                    Column {
                        Text(
                            text = "👤 YOUR NAME",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = playerName,
                            onValueChange = { viewModel.playerNameInput.value = it },
                            placeholder = { Text("Enter your name...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("join_name_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ChemCyan,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            ),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "CHOOSE AVATAR",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            QuestionsData.AVATARS.take(6).forEach { av ->
                                val isSelected = selectedAvatar == av
                                Surface(
                                    shape = CircleShape,
                                    color = if (isSelected) ChemCyan.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surface,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.5.dp,
                                        if (isSelected) ChemCyan else MaterialTheme.colorScheme.outline
                                    ),
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clickable { viewModel.selectedAvatar.value = av }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(text = av, fontSize = 20.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Join Action
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    ChemPrimaryButton(
                        text = "JOIN GAME →",
                        onClick = { viewModel.joinOnlineRoom() },
                        enabled = roomCode.isNotBlank(),
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "join_game_submit_btn"
                    )
                    ChemSecondaryButton(
                        text = "← Back to Home",
                        onClick = { viewModel.navigateTo(ScreenState.HOME) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
