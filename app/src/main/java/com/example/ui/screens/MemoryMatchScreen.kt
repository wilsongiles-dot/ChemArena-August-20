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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ChemCard
import com.example.ui.components.MemoryCardView
import com.example.ui.theme.ChemCyan
import com.example.ui.theme.ChemGreen
import com.example.ui.theme.ChemOrange
import com.example.ui.theme.ChemPurple
import com.example.ui.theme.ChemRed
import com.example.ui.viewmodels.ChemViewModel

@Composable
fun MemoryMatchScreen(
    viewModel: ChemViewModel,
    modifier: Modifier = Modifier
) {
    val memoryCards by viewModel.memoryCards.collectAsState()
    val moves by viewModel.memoryMoves.collectAsState()
    val matchedPairs by viewModel.memoryMatchedPairs.collectAsState()
    val timerSeconds by viewModel.memoryTimerSeconds.collectAsState()

    var showExitDialog by remember { mutableStateOf(false) }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Quit Memory Match?", fontWeight = FontWeight.Bold) },
            text = { Text("Your current match score will not be saved.") },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    viewModel.leaveMatch()
                }) {
                    Text("Quit", color = ChemRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Continue")
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
                                contentDescription = "Exit Memory Game",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Memory Match",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = ChemPurple
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatPill(label = "MOVES", value = "$moves")
                        StatPill(label = "MATCHED", value = "$matchedPairs / 6", color = ChemGreen)
                        StatPill(label = "TIME", value = "${timerSeconds}s", color = ChemCyan)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Instructions Banner
            Text(
                text = "Tap cards to flip & match terms with definitions",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Memory Cards 3x4 Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(memoryCards) { card ->
                    MemoryCardView(
                        text = card.text,
                        isFlipped = card.isFlipped,
                        isMatched = card.isMatched,
                        onClick = { viewModel.onMemoryCardClick(card.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun StatPill(
    label: String,
    value: String,
    color: androidx.compose.ui.graphics.Color = ChemOrange
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$label: ",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
        }
    }
}
