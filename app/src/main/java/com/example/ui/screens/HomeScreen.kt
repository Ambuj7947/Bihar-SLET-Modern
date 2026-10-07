package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DefaultQuestionBank
import com.example.ui.MainTab
import com.example.ui.QuizViewModel
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CorrectGreen
import com.example.ui.theme.PrimaryNavy
import com.example.ui.theme.WrongRed

@Composable
fun HomeScreen(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier
) {
    val allQuestions by viewModel.allQuestions.collectAsState()
    val mistakes by viewModel.mistakeQuestions.collectAsState()
    val bookmarks by viewModel.bookmarkedQuestions.collectAsState()
    val attempts by viewModel.allAttempts.collectAsState()

    val totalAttemptedQuestions = allQuestions.count { it.timesAttempted > 0 }
    val totalCorrect = allQuestions.sumOf { it.timesCorrect }
    val totalAttempts = allQuestions.sumOf { it.timesAttempted }
    val overallAccuracy = if (totalAttempts > 0) ((totalCorrect * 100) / totalAttempts) else 0

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Testbook Banner: Full Mock Test Pass
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF0F3D7C), Color(0xFF1E5BB0))
                        )
                    )
                    .testTag("hero_banner_card")
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFFBBF24))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "TESTBOOK STYLE TEST SERIES",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF78350F)
                            )
                        }

                        Text(
                            text = "BLET / KVS 2026",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }

                    Text(
                        text = "Library Science Mock Test #1",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = "20 Questions • 20 Mins • Real Exam Simulation with Negative Marking (-0.25)",
                        fontSize = 12.5.sp,
                        color = Color.White.copy(alpha = 0.85f),
                        lineHeight = 18.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { viewModel.startFullMockTest(1) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF59E0B),
                                contentColor = Color(0xFF0F172A)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("start_mock_test_1_btn")
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Start Mock Test", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }

                        OutlinedButton(
                            onClick = { viewModel.startDailyQuiz() },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(0.9f)
                                .height(46.dp)
                                .testTag("start_daily_quiz_btn")
                        ) {
                            Icon(Icons.Default.Today, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Daily Quiz", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Live Performance Quick Stats Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickStatCard(
                    title = "Attempted",
                    value = "$totalAttemptedQuestions / ${allQuestions.size}",
                    subtitle = "Questions practiced",
                    accentColor = PrimaryNavy,
                    modifier = Modifier.weight(1f)
                )
                QuickStatCard(
                    title = "Accuracy",
                    value = "$overallAccuracy%",
                    subtitle = "Overall precision",
                    accentColor = CorrectGreen,
                    modifier = Modifier.weight(1f)
                )
                QuickStatCard(
                    title = "Tests Taken",
                    value = "${attempts.size}",
                    subtitle = "Completed attempts",
                    accentColor = AmberAccent,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Action Hub (Mistakes, Bookmarks, Test Series, Notes)
        item {
            Text(
                text = "Target Practice Hub",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                ),
                color = Color(0xFF0F172A)
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ActionCard(
                    title = "Mistake Notebook",
                    count = "${mistakes.size} Questions",
                    icon = Icons.Default.ErrorOutline,
                    badgeColor = WrongRed,
                    onClick = {
                        viewModel.selectTab(MainTab.SAVED)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("action_mistakes_card")
                )

                ActionCard(
                    title = "Saved Bookmarks",
                    count = "${bookmarks.size} Questions",
                    icon = Icons.Default.Bookmark,
                    badgeColor = AmberAccent,
                    onClick = {
                        viewModel.selectTab(MainTab.SAVED)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("action_bookmarks_card")
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ActionCard(
                    title = "Test Series & Mocks",
                    count = "18+ Exam Papers",
                    icon = Icons.Default.Quiz,
                    badgeColor = Color(0xFF2563EB),
                    onClick = {
                        viewModel.selectTab(MainTab.TEST_SERIES)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("action_test_series_card")
                )

                ActionCard(
                    title = "Study Notes",
                    count = "High-Yield Summary",
                    icon = Icons.Default.AutoStories,
                    badgeColor = Color(0xFF7C3AED),
                    onClick = {
                        viewModel.selectTab(MainTab.NOTES)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("action_notes_card")
                )
            }
        }

        // Sectional Syllabus Units
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Syllabus Units & Sectional Tests",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    ),
                    color = Color(0xFF0F172A)
                )

                Text(
                    text = "6 Units",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy
                )
            }
        }

        // Unit Cards List
        itemsIndexed(DefaultQuestionBank.allUnits) { index, unitName ->
            val unitQuestions = allQuestions.filter { it.category == unitName }
            val unitAttempted = unitQuestions.count { it.timesAttempted > 0 }
            val unitCorrect = unitQuestions.sumOf { it.timesCorrect }
            val unitAcc = if (unitAttempted > 0) (unitCorrect * 100) / unitAttempted else 0

            UnitPracticeCard(
                unitIndex = index + 1,
                unitTitle = unitName,
                questionCount = unitQuestions.size,
                attemptedCount = unitAttempted,
                accuracy = unitAcc,
                onStartTest = { viewModel.startSectionalTest(unitName) },
                onStartPractice = { viewModel.startTopicPracticeMode(unitName) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun QuickStatCard(
    title: String,
    value: String,
    subtitle: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF64748B)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = Color(0xFF94A3B8)
            )
        }
    }
}

@Composable
private fun ActionCard(
    title: String,
    count: String,
    icon: ImageVector,
    badgeColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(badgeColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = badgeColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
                Text(
                    text = count,
                    fontSize = 11.5.sp,
                    color = Color(0xFF64748B)
                )
            }
        }
    }
}

@Composable
private fun UnitPracticeCard(
    unitIndex: Int,
    unitTitle: String,
    questionCount: Int,
    attemptedCount: Int,
    accuracy: Int,
    onStartTest: () -> Unit,
    onStartPractice: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
            .testTag("unit_card_$unitIndex")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
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
                        text = "UNIT 0$unitIndex",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1D4ED8)
                    )
                }

                if (attemptedCount > 0) {
                    Text(
                        text = "$accuracy% Accuracy",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (accuracy >= 70) CorrectGreen else AmberAccent
                    )
                } else {
                    Text(
                        text = "$questionCount MCQs",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            Text(
                text = unitTitle,
                fontSize = 15.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                lineHeight = 22.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onStartPractice,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .testTag("practice_unit_${unitIndex}_btn")
                ) {
                    Text("Practice / DPP", fontWeight = FontWeight.SemiBold, fontSize = 12.5.sp)
                }

                Button(
                    onClick = onStartTest,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1.1f)
                        .height(42.dp)
                        .testTag("test_unit_${unitIndex}_btn")
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Timed Test", fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
                }
            }
        }
    }
}
