package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ChemCyan
import com.example.ui.theme.ChemGreen
import com.example.ui.theme.ChemOrange
import com.example.ui.theme.ChemPurple
import com.example.ui.theme.ChemRed
import com.example.ui.theme.ChemYellow

@Composable
fun TacticalBuzzerButton(
    enabled: Boolean,
    buzzedPlayerName: String?,
    isMyBuzz: Boolean,
    onBuzz: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (enabled && buzzedPlayerName == null) 1.05f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val buttonScale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = tween(80),
        label = "pressScale"
    )

    val effectiveScale = (if (isPressed) buttonScale else pulseScale)

    val buzzerColor = when {
        isMyBuzz -> ChemCyan
        buzzedPlayerName != null -> ChemOrange
        enabled -> ChemCyan
        else -> MaterialTheme.colorScheme.outline
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (buzzedPlayerName != null) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isMyBuzz) ChemCyan.copy(alpha = 0.15f) else ChemOrange.copy(alpha = 0.15f),
                border = BorderStroke(1.5.dp, if (isMyBuzz) ChemCyan else ChemOrange),
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = if (isMyBuzz) "🔔 You buzzed in! Select your answer now." else "🔔 $buzzedPlayerName buzzed in! Answering...",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isMyBuzz) ChemCyan else ChemOrange,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .size(130.dp)
                .scale(effectiveScale)
                .shadow(
                    elevation = if (enabled && buzzedPlayerName == null) 16.dp else 4.dp,
                    shape = CircleShape,
                    ambientColor = buzzerColor,
                    spotColor = buzzerColor
                )
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            buzzerColor.copy(alpha = if (enabled) 0.4f else 0.15f),
                            buzzerColor.copy(alpha = if (enabled) 0.85f else 0.3f)
                        )
                    )
                )
                .border(4.dp, buzzerColor, CircleShape)
                .clickable(
                    enabled = enabled && buzzedPlayerName == null,
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onBuzz()
                }
                .testTag("buzzer_button"),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "🔔", fontSize = 34.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (buzzedPlayerName != null) "LOCKED" else "BUZZ IN",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun MemoryCardView(
    text: String,
    isFlipped: Boolean,
    isMatched: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped || isMatched) 180f else 0f,
        animationSpec = tween(350),
        label = "cardFlip"
    )

    val isFrontVisible = rotation > 90f

    val cardBorder = when {
        isMatched -> ChemGreen
        isFlipped -> ChemCyan
        else -> MaterialTheme.colorScheme.outline
    }

    val cardBg = when {
        isMatched -> ChemGreen.copy(alpha = 0.2f)
        isFlipped -> MaterialTheme.colorScheme.surfaceVariant
        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(if (isFlipped || isMatched) 2.dp else 1.dp, cardBorder),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        modifier = modifier
            .height(95.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clickable(enabled = !isFlipped && !isMatched) {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onClick()
            }
            .testTag("memory_card")
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isFrontVisible) {
                Text(
                    text = text,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = if (isMatched) ChemGreen else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.graphicsLayer { rotationY = 180f }
                )
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "⚛️", fontSize = 22.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "FLIP",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}
