package com.example.ui.screens

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ChemCard
import com.example.ui.components.ChemPrimaryButton
import com.example.ui.components.ChemSecondaryButton
import com.example.ui.theme.ChemCyan
import com.example.ui.theme.ChemOrange
import com.example.ui.viewmodels.ChemViewModel

@Composable
fun WaitingRoomScreen(
    viewModel: ChemViewModel,
    modifier: Modifier = Modifier
) {
    val roomCode by viewModel.roomCode.collectAsState()
    val isHost by viewModel.isHost.collectAsState()
    val players by viewModel.players.collectAsState()
    val myPlayerId by viewModel.myPlayerId.collectAsState()
    val gameMode by viewModel.selectedGameMode.collectAsState()
    val activeQuestions by viewModel.activeQuestions.collectAsState()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 520.dp)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Waiting Room",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Room Code Display
            item {
                ChemCard(borderColor = ChemCyan) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "ROOM CODE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = roomCode,
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 8.sp,
                            color = ChemCyan
                        )
                        Text(
                            text = "Share this 4-digit code with friends or classmates",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Players List
            item {
                ChemCard {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "PLAYERS JOINED",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = ChemCyan.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "${players.size} in lobby",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ChemCyan,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            players.forEach { p ->
                                val isMe = p.id == myPlayerId
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isMe) ChemCyan.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isMe) ChemCyan else MaterialTheme.colorScheme.outline
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = ChemCyan.copy(alpha = 0.15f),
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(text = p.avatar, fontSize = 16.sp)
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = p.name,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        if (isMe) {
                                            Text(
                                                text = " (You)",
                                                fontSize = 12.sp,
                                                color = ChemCyan
                                            )
                                        }
                                        Spacer(modifier = Modifier.weight(1f))
                                        if (p.host) {
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = ChemOrange
                                            ) {
                                                Text(
                                                    text = "HOST",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = androidx.compose.ui.graphics.Color.White,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Status Info & Controls
            item {
                if (isHost) {
                    ChemCard {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Mode: ${gameMode.uppercase()} · ${activeQuestions.size} Questions",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            ChemPrimaryButton(
                                text = "START MATCH NOW →",
                                onClick = { viewModel.startOnlineGameFromWaiting() },
                                modifier = Modifier.fillMaxWidth(),
                                testTag = "start_online_game_btn"
                            )
                        }
                    }
                } else {
                    ChemCard {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = ChemCyan,
                                strokeWidth = 2.5.dp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Waiting for host to start the battle...",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            item {
                ChemSecondaryButton(
                    text = "Leave Room",
                    onClick = { viewModel.leaveMatch() },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
