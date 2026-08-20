package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.Player
import com.example.ui.theme.ChemCyan
import com.example.ui.theme.ChemGreen
import com.example.ui.theme.ChemOrange
import com.example.ui.theme.ChemPurple
import com.example.ui.theme.ChemRed
import com.example.ui.theme.ChemYellow

@Composable
fun ChemPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    testTag: String = "primary_button"
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = tween(100),
        label = "btnScale"
    )

    Button(
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            onClick()
        },
        enabled = enabled,
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = accentColor,
            contentColor = textColor,
            disabledContainerColor = accentColor.copy(alpha = 0.35f),
            disabledContentColor = textColor.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .scale(scale)
            .height(52.dp)
            .testTag(testTag)
    ) {
        Text(
            text = text,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun ChemSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    testTag: String = "secondary_button"
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = tween(100),
        label = "btnScale"
    )

    OutlinedButton(
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            onClick()
        },
        enabled = enabled,
        interactionSource = interactionSource,
        border = BorderStroke(1.5.dp, if (enabled) borderColor else borderColor.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
            .scale(scale)
            .height(50.dp)
            .testTag(testTag)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun ChemCard(
    modifier: Modifier = Modifier,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = modifier
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}

@Composable
fun TopicChip(
    text: String,
    isSelected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val bgColor = if (isSelected) ChemCyan.copy(alpha = 0.18f) else MaterialTheme.colorScheme.surfaceVariant
    val borderColor = if (isSelected) ChemCyan else MaterialTheme.colorScheme.outline
    val textColor = if (isSelected) ChemCyan else MaterialTheme.colorScheme.onSurfaceVariant

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = bgColor,
        border = BorderStroke(1.2.dp, borderColor),
        modifier = modifier
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onToggle()
            }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = ChemCyan,
                    modifier = Modifier
                        .size(14.dp)
                        .padding(end = 4.dp)
                )
            }
            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = textColor
            )
        }
    }
}

@Composable
fun TimerProgressBar(
    timeLeft: Int,
    totalSeconds: Int,
    modifier: Modifier = Modifier
) {
    val fraction = if (totalSeconds > 0) (timeLeft.toFloat() / totalSeconds.toFloat()).coerceIn(0f, 1f) else 0f
    val barColor = when {
        fraction < 0.25f -> ChemRed
        fraction < 0.55f -> ChemYellow
        else -> ChemCyan
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "⏳ TIME REMAINING",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${timeLeft}s",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = barColor
            )
        }
        LinearProgressIndicator(
            progress = { fraction },
            color = barColor,
            trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
        )
    }
}

@Composable
fun ScoreboardRow(
    players: List<Player>,
    myId: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        players.forEach { p ->
            val isMe = p.id == myId
            val borderColor = if (isMe) ChemCyan else MaterialTheme.colorScheme.outline

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (isMe) ChemCyan.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant,
                border = BorderStroke(if (isMe) 1.5.dp else 1.dp, borderColor),
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = p.avatar, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isMe) "${p.name.take(6)} (You)" else p.name.take(8),
                            fontSize = 11.sp,
                            fontWeight = if (isMe) FontWeight.Bold else FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = if (isMe) ChemCyan else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "${p.score}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isMe) ChemCyan else ChemOrange
                    )
                }
            }
        }
    }
}

@Composable
fun FeedbackCard(
    isCorrect: Boolean,
    message: String,
    explanation: String = "",
    modifier: Modifier = Modifier
) {
    val bgColor = if (isCorrect) ChemGreen.copy(alpha = 0.12f) else ChemRed.copy(alpha = 0.12f)
    val borderColor = if (isCorrect) ChemGreen else ChemRed
    val icon = if (isCorrect) Icons.Default.Check else Icons.Default.Close
    val iconColor = if (isCorrect) ChemGreen else ChemRed

    Card(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.5.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier
                    .size(24.dp)
                    .padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = message,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = iconColor
                )
                if (explanation.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = explanation,
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
