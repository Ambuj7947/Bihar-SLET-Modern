package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CorrectGreen
import com.example.ui.theme.PrimaryNavy
import com.example.ui.theme.WrongRed

data class UnitPerformance(
    val unitName: String,
    val shortLabel: String,
    val totalQuestions: Int,
    val attemptedQuestions: Int,
    val correctQuestions: Int,
    val color: Color
) {
    val accuracy: Int
        get() = if (attemptedQuestions > 0) ((correctQuestions * 100) / attemptedQuestions).coerceIn(0, 100) else 0

    val masteryLevel: String
        get() = when {
            attemptedQuestions == 0 -> "Not Started"
            accuracy >= 75 -> "Strong"
            accuracy >= 50 -> "Moderate"
            else -> "Needs Focus"
        }
}

@Composable
fun CircularAccuracyGauge(
    accuracy: Int,
    modifier: Modifier = Modifier
) {
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(accuracy) {
        animatedProgress.animateTo(
            targetValue = (accuracy / 100f).coerceIn(0f, 1f),
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
        )
    }

    val gaugeColor = when {
        accuracy >= 75 -> CorrectGreen
        accuracy >= 50 -> AmberAccent
        else -> WrongRed
    }

    Box(
        modifier = modifier.size(110.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(100.dp)) {
            val strokeWidth = 10.dp.toPx()
            val radius = (size.minDimension - strokeWidth) / 2
            val centerOffset = Offset(size.width / 2, size.height / 2)

            // Background Track
            drawArc(
                color = Color(0xFFF1F5F9),
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                topLeft = Offset(centerOffset.x - radius, centerOffset.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Active Progress
            drawArc(
                color = gaugeColor,
                startAngle = 135f,
                sweepAngle = 270f * animatedProgress.value,
                useCenter = false,
                topLeft = Offset(centerOffset.x - radius, centerOffset.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$accuracy%",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xFF0F172A)
            )
            Text(
                text = "Accuracy",
                fontSize = 11.sp,
                color = Color(0xFF64748B)
            )
        }
    }
}

@Composable
fun UnitBreakdownRow(
    item: UnitPerformance,
    modifier: Modifier = Modifier
) {
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(item.accuracy) {
        animatedProgress.animateTo(
            targetValue = item.accuracy / 100f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )
    }

    val badgeBg = when (item.masteryLevel) {
        "Strong" -> Color(0xFFDCFCE7)
        "Moderate" -> Color(0xFFFEF3C7)
        "Needs Focus" -> Color(0xFFFEE2E2)
        else -> Color(0xFFF1F5F9)
    }

    val badgeColor = when (item.masteryLevel) {
        "Strong" -> Color(0xFF15803D)
        "Moderate" -> Color(0xFFB45309)
        "Needs Focus" -> Color(0xFFB91C1C)
        else -> Color(0xFF64748B)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(item.color)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = item.shortLabel,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.5.sp,
                    color = Color(0xFF1E293B),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${item.correctQuestions}/${item.attemptedQuestions} correct",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(badgeBg)
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = item.masteryLevel,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeColor
                    )
                }
            }
        }

        // Custom Smooth Progress Bar
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        ) {
            val width = size.width
            val height = size.height

            drawRoundRect(
                color = Color(0xFFF1F5F9),
                size = Size(width, height),
                cornerRadius = CornerRadius(height / 2, height / 2)
            )

            val fillWidth = width * animatedProgress.value
            if (fillWidth > 0f) {
                drawRoundRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(item.color.copy(alpha = 0.8f), item.color)
                    ),
                    size = Size(fillWidth, height),
                    cornerRadius = CornerRadius(height / 2, height / 2)
                )
            }
        }
    }
}
