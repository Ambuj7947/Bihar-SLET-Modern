package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CorrectGreen
import com.example.ui.theme.PrimaryNavy
import com.example.ui.theme.ReviewPurple
import com.example.ui.theme.WrongRed

enum class QuestionStatus {
    NOT_VISITED,
    NOT_ANSWERED,
    ANSWERED,
    MARKED_FOR_REVIEW,
    ANSWERED_AND_MARKED_FOR_REVIEW
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun QuestionPaletteBottomSheet(
    totalQuestions: Int,
    currentQuestionIndex: Int,
    questionStatuses: List<QuestionStatus>,
    onSelectQuestion: (Int) -> Unit,
    onSubmitClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
                .navigationBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Question Palette",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "प्रश्न स्थिति सारांश",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFE2E8F0))

            // Legend Grid
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                val answeredCount = questionStatuses.count { it == QuestionStatus.ANSWERED }
                val notAnsweredCount = questionStatuses.count { it == QuestionStatus.NOT_ANSWERED }
                val reviewCount = questionStatuses.count { it == QuestionStatus.MARKED_FOR_REVIEW }
                val ansReviewCount = questionStatuses.count { it == QuestionStatus.ANSWERED_AND_MARKED_FOR_REVIEW }
                val notVisitedCount = questionStatuses.count { it == QuestionStatus.NOT_VISITED }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    LegendItem(count = answeredCount, label = "Answered", color = CorrectGreen)
                    LegendItem(count = notAnsweredCount, label = "Not Answered", color = WrongRed)
                    LegendItem(count = reviewCount, label = "Marked", color = ReviewPurple)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    LegendItem(count = ansReviewCount, label = "Ans & Marked", color = Color(0xFF6D28D9), hasDot = true)
                    LegendItem(count = notVisitedCount, label = "Not Visited", color = Color(0xFF94A3B8))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Scrollable Question Matrix Grid
            Column(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .height(260.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (i in 0 until totalQuestions) {
                        val status = questionStatuses.getOrElse(i) { QuestionStatus.NOT_VISITED }
                        val isCurrent = i == currentQuestionIndex

                        PaletteBubble(
                            index = i + 1,
                            status = status,
                            isCurrent = isCurrent,
                            onClick = {
                                onSelectQuestion(i)
                                onDismiss()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Submit Test CTA
            Button(
                onClick = onSubmitClick,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("palette_submit_test_button")
            ) {
                Text("Submit Test / टेस्ट सबमिट करें", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}

@Composable
private fun LegendItem(
    count: Int,
    label: String,
    color: Color,
    hasDot: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            if (hasDot) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(CorrectGreen)
                )
            }
        }
        Text(
            text = "$count $label",
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF334155)
        )
    }
}

@Composable
private fun PaletteBubble(
    index: Int,
    status: QuestionStatus,
    isCurrent: Boolean,
    onClick: () -> Unit
) {
    val (bgColor, textColor) = when (status) {
        QuestionStatus.ANSWERED -> Pair(CorrectGreen, Color.White)
        QuestionStatus.NOT_ANSWERED -> Pair(WrongRed, Color.White)
        QuestionStatus.MARKED_FOR_REVIEW -> Pair(ReviewPurple, Color.White)
        QuestionStatus.ANSWERED_AND_MARKED_FOR_REVIEW -> Pair(Color(0xFF6D28D9), Color.White)
        QuestionStatus.NOT_VISITED -> Pair(Color(0xFFF1F5F9), Color(0xFF475569))
    }

    val borderWidth = if (isCurrent) 2.5.dp else 1.dp
    val borderColor = if (isCurrent) Color(0xFF2563EB) else Color.Transparent

    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .border(borderWidth, borderColor, RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .testTag("palette_question_$index"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$index",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )

        if (status == QuestionStatus.ANSWERED_AND_MARKED_FOR_REVIEW) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4ADE80))
                    .align(Alignment.TopEnd)
            )
        }
    }
}
