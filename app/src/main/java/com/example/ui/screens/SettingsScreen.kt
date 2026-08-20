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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
import com.example.data.models.QuestionsData
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: ChemViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    var editName by remember(userProfile) { mutableStateOf(userProfile?.playerName ?: "") }
    var editAvatar by remember(userProfile) { mutableStateOf(userProfile?.avatar ?: "🧪") }
    var editColor by remember(userProfile) { mutableStateOf(userProfile?.color ?: "#00E5FF") }
    var restoreIdInput by remember { mutableStateOf("") }

    val currentTheme = userProfile?.themeMode ?: "system"

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
                        text = "Settings & Cloud Sync",
                        fontSize = 18.sp,
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
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "APPEARANCE & DARK MODE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Dark Mode Options
                item {
                    ChemCard {
                        Column {
                            Text(
                                text = "Choose Visual Theme",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                ThemeButton(
                                    label = "Dark",
                                    icon = Icons.Default.DarkMode,
                                    isSelected = currentTheme == "dark",
                                    modifier = Modifier.weight(1f),
                                    onClick = { viewModel.updateThemeMode("dark") }
                                )
                                ThemeButton(
                                    label = "Light",
                                    icon = Icons.Default.LightMode,
                                    isSelected = currentTheme == "light",
                                    modifier = Modifier.weight(1f),
                                    onClick = { viewModel.updateThemeMode("light") }
                                )
                                ThemeButton(
                                    label = "Auto",
                                    icon = Icons.Default.SettingsBrightness,
                                    isSelected = currentTheme == "system",
                                    modifier = Modifier.weight(1f),
                                    onClick = { viewModel.updateThemeMode("system") }
                                )
                            }
                        }
                    }
                }

                // Push Notifications
                item {
                    Text(
                        text = "PUSH NOTIFICATIONS & STUDY REMINDERS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                item {
                    ChemCard {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = null,
                                        tint = ChemOrange,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "Daily Study Streak Alerts",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "Reminds you to practice QCAA concepts daily",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            ChemSecondaryButton(
                                text = "🔔 Send Test Study Alert Notification",
                                onClick = { viewModel.testReminderNotification() },
                                modifier = Modifier.fillMaxWidth(),
                                testTag = "test_notification_btn"
                            )
                        }
                    }
                }

                // Profile Editor
                item {
                    Text(
                        text = "STUDENT PROFILE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                item {
                    ChemCard {
                        Column {
                            OutlinedTextField(
                                value = editName,
                                onValueChange = { editName = it },
                                label = { Text("Display Name / Tag") },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ChemCyan,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Choose Profile Avatar",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                QuestionsData.AVATARS.forEach { av ->
                                    val isSelected = editAvatar == av
                                    Surface(
                                        shape = CircleShape,
                                        color = if (isSelected) ChemCyan.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surface,
                                        border = BorderStroke(1.5.dp, if (isSelected) ChemCyan else MaterialTheme.colorScheme.outline),
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clickable { editAvatar = av }
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(text = av, fontSize = 18.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            ChemPrimaryButton(
                                text = "Save Profile Changes",
                                onClick = { viewModel.updateProfileInfo(editName, editAvatar, editColor) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                // Cloud Save Syncing
                item {
                    Text(
                        text = "CLOUD SAVE & PROFILE SYNC",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                item {
                    ChemCard {
                        Column {
                            Text(
                                text = "Sync your streak, high scores, and topic stats across devices using Firebase Cloud Storage.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            val lastSync = userProfile?.lastSyncTimestamp ?: 0L
                            if (lastSync > 0) {
                                val syncDate = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date(lastSync))
                                Text(
                                    text = "Last synced: $syncDate",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ChemGreen,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            ChemPrimaryButton(
                                text = "Backup Profile to Cloud ☁️",
                                onClick = { viewModel.backupProfile() },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "Restore from Cloud Tag:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = restoreIdInput,
                                    onValueChange = { restoreIdInput = it },
                                    placeholder = { Text("Enter student name...") },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = ChemCyan,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f)
                                )
                                ChemSecondaryButton(
                                    text = "Restore",
                                    onClick = {
                                        if (restoreIdInput.isNotBlank()) {
                                            viewModel.restoreProfile(restoreIdInput.trim())
                                        }
                                    },
                                    modifier = Modifier.width(100.dp)
                                )
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
fun ThemeButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) ChemCyan.copy(alpha = 0.18f) else MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.5.dp, if (isSelected) ChemCyan else MaterialTheme.colorScheme.outline),
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) ChemCyan else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) ChemCyan else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
