package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuestionEntity
import com.example.ui.QuizViewModel
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CorrectGreen
import com.example.ui.theme.CorrectGreenBg
import com.example.ui.theme.PrimaryNavy
import com.example.ui.theme.WrongRed
import com.example.ui.theme.WrongRedBg

@Composable
fun SavedMistakesScreen(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Mistake Notebook, 1 = Bookmarks
    val mistakes by viewModel.mistakeQuestions.collectAsState()
    val bookmarks by viewModel.bookmarkedQuestions.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = PrimaryNavy,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Text(
                        text = "Mistakes (${mistakes.size})",
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.5.sp
                    )
                },
                modifier = Modifier.testTag("tab_mistakes")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Text(
                        text = "Bookmarks (${bookmarks.size})",
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.5.sp
                    )
                },
                modifier = Modifier.testTag("tab_bookmarks")
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (selectedTab == 0) {
                // Mistake Notebook Mode
                if (mistakes.isNotEmpty()) {
                    item {
                        Button(
                            onClick = { viewModel.startMistakeRevision() },
                            colors = ButtonDefaults.buttonColors(containerColor = WrongRed),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("start_mistake_quiz_btn")
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Re-attempt All Mistakes (${mistakes.size} Qs)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    items(mistakes) { question ->
                        DetailedQuestionStudyCard(
                            question = question,
                            onToggleBookmark = { viewModel.toggleBookmark(question) }
                        )
                    }
                } else {
                    item {
                        EmptyStateCard(
                            icon = Icons.Default.CheckCircle,
                            iconColor = CorrectGreen,
                            title = "Mistake Notebook is Empty!",
                            message = "Great job! You haven't made any mistakes yet or all mistakes were corrected."
                        )
                    }
                }
            } else {
                // Bookmarks Mode
                if (bookmarks.isNotEmpty()) {
                    item {
                        Button(
                            onClick = { viewModel.startBookmarkedQuiz() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AmberAccent,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("start_bookmarked_quiz_btn")
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Practice Bookmarked Questions (${bookmarks.size})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    items(bookmarks) { question ->
                        DetailedQuestionStudyCard(
                            question = question,
                            onToggleBookmark = { viewModel.toggleBookmark(question) }
                        )
                    }
                } else {
                    item {
                        EmptyStateCard(
                            icon = Icons.Default.BookmarkBorder,
                            iconColor = Color(0xFF94A3B8),
                            title = "No Bookmarks Saved Yet",
                            message = "Tap the bookmark icon on any question during tests to save it for quick revision."
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun DetailedQuestionStudyCard(
    question: QuestionEntity,
    onToggleBookmark: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header badge & bookmark
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFEFF6FF))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = question.category,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1D4ED8)
                    )
                }

                IconButton(
                    onClick = onToggleBookmark,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (question.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Toggle Bookmark",
                        tint = if (question.isBookmarked) AmberAccent else Color(0xFF94A3B8)
                    )
                }
            }

            // Question Hindi
            Text(
                text = question.questionHindi,
                fontSize = 15.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                lineHeight = 22.sp
            )

            // Question English if present
            if (question.questionEnglish.isNotBlank()) {
                Text(
                    text = question.questionEnglish,
                    fontSize = 13.sp,
                    color = Color(0xFF475569),
                    lineHeight = 18.sp
                )
            }

            // Options with Correct Answer highlighted
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                for (optIndex in 1..4) {
                    val optText = question.getOptionText(optIndex)
                    val isCorrect = optIndex == question.correctOption
                    val isLastWrong = question.timesAttempted > 0 && optIndex == question.lastAttemptOption && !isCorrect

                    val (rowBg, rowBorder, textColor) = when {
                        isCorrect -> Triple(CorrectGreenBg, CorrectGreen, Color(0xFF14532D))
                        isLastWrong -> Triple(WrongRedBg, WrongRed, Color(0xFF7F1D1D))
                        else -> Triple(Color(0xFFF8FAFC), Color(0xFFE2E8F0), Color(0xFF334155))
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(rowBg)
                            .border(1.dp, rowBorder, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val letter = when (optIndex) {
                            1 -> "A"
                            2 -> "B"
                            3 -> "C"
                            4 -> "D"
                            else -> ""
                        }

                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(if (isCorrect) CorrectGreen else if (isLastWrong) WrongRed else Color(0xFFE2E8F0)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isCorrect) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            } else if (isLastWrong) {
                                Icon(Icons.Default.Close, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            } else {
                                Text(letter, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = optText,
                            fontSize = 14.sp,
                            fontWeight = if (isCorrect) FontWeight.SemiBold else FontWeight.Normal,
                            color = textColor,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Explanation Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF0FDF4))
                    .border(1.dp, Color(0xFFBBF7D0), RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Detailed Solution & Rationale:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF15803D)
                    )
                    Text(
                        text = question.explanation,
                        fontSize = 13.5.sp,
                        color = Color(0xFF1E293B),
                        lineHeight = 20.sp
                    )

                    if (question.keyHighlight.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = AmberAccent, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = question.keyHighlight,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFB45309)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyStateCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    title: String,
    message: String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF1E293B)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = message,
                fontSize = 13.sp,
                color = Color(0xFF64748B),
                lineHeight = 18.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
