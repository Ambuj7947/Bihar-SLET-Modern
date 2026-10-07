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
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DefaultQuestionBank
import com.example.ui.QuizViewModel
import com.example.ui.components.CircularAccuracyGauge
import com.example.ui.components.UnitBreakdownRow
import com.example.ui.components.UnitPerformance
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CorrectGreen
import com.example.ui.theme.PrimaryNavy
import com.example.ui.theme.WrongRed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AnalyticsScreen(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier
) {
    val allQuestions by viewModel.allQuestions.collectAsState()
    val allAttempts by viewModel.allAttempts.collectAsState()

    val totalAttempted = allQuestions.count { it.timesAttempted > 0 }
    val totalCorrect = allQuestions.sumOf { it.timesCorrect }
    val totalAttemptsCount = allQuestions.sumOf { it.timesAttempted }
    val accuracy = if (totalAttemptsCount > 0) ((totalCorrect * 100) / totalAttemptsCount) else 0

    // Estimated Percentile calculation based on accuracy and questions attempted
    val estimatedPercentile = when {
        totalAttempted == 0 -> 0
        accuracy >= 85 -> 92
        accuracy >= 70 -> 84
        accuracy >= 55 -> 68
        accuracy >= 40 -> 45
        else -> 28
    }

    val unitColors = listOf(
        Color(0xFF2563EB), // Unit 1 Blue
        Color(0xFF0D9488), // Unit 2 Teal
        Color(0xFF7C3AED), // Unit 3 Violet
        Color(0xFFD97706), // Unit 4 Amber
        Color(0xFFDC2626), // Unit 5 Red
        Color(0xFF059669)  // Unit 6 Green
    )

    val unitPerformances = remember(allQuestions) {
        DefaultQuestionBank.allUnits.mapIndexed { index, unitName ->
            val uQuestions = allQuestions.filter { it.category == unitName }
            val uAttempted = uQuestions.count { it.timesAttempted > 0 }
            val uCorrect = uQuestions.sumOf { it.timesCorrect }
            val color = unitColors.getOrElse(index) { PrimaryNavy }

            val shortLabel = when (index) {
                0 -> "Unit 1: Foundations"
                1 -> "Unit 2: Classification"
                2 -> "Unit 3: Management"
                3 -> "Unit 4: Info Sources"
                4 -> "Unit 5: ICT & Autom."
                else -> "Unit 6: Mock Papers"
            }

            UnitPerformance(
                unitName = unitName,
                shortLabel = shortLabel,
                totalQuestions = uQuestions.size,
                attemptedQuestions = uAttempted,
                correctQuestions = uCorrect,
                color = color
            )
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Overall Scorecard Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(18.dp))
                    .testTag("overall_analytics_card")
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(PrimaryNavy.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Analytics,
                                    contentDescription = null,
                                    tint = PrimaryNavy,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Performance Overview",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = "Testbook Analytics Dashboard",
                                    fontSize = 11.5.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        if (totalAttempted > 0) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFFEF3C7))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "~$estimatedPercentile%ile Rank",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF92400E)
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CircularAccuracyGauge(accuracy = accuracy)

                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f).padding(start = 16.dp)
                        ) {
                            MetricPill(
                                label = "Total Practiced",
                                value = "$totalAttempted / ${allQuestions.size} Qs",
                                color = PrimaryNavy
                            )
                            MetricPill(
                                label = "Correct Answers",
                                value = "$totalCorrect",
                                color = CorrectGreen
                            )
                            MetricPill(
                                label = "Total Tests Taken",
                                value = "${allAttempts.size} Tests",
                                color = AmberAccent
                            )
                        }
                    }
                }
            }
        }

        // Sectional Strength & Mastery Level
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(18.dp))
                    .testTag("sectional_mastery_card")
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Unit-wise Strength & Accuracy",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "6 Syllabus Units",
                            fontSize = 11.5.sp,
                            color = Color(0xFF64748B)
                        )
                    }

                    unitPerformances.forEach { unitPerf ->
                        UnitBreakdownRow(item = unitPerf)
                    }
                }
            }
        }

        // Test History Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Test Attempts History",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = "${allAttempts.size} Completed",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )
            }
        }

        if (allAttempts.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "No test attempts yet",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF1E293B)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Start a full mock test or sectional test to see your analytics scorecard.",
                            fontSize = 12.5.sp,
                            color = Color(0xFF64748B),
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { viewModel.startFullMockTest(1) },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Take Full Mock Test", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        } else {
            items(allAttempts) { attempt ->
                TestHistoryItemCard(attempt = attempt)
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun MetricPill(
    label: String,
    value: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 11.5.sp,
            color = Color(0xFF64748B)
        )
        Text(
            text = value,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
private fun TestHistoryItemCard(
    attempt: com.example.data.model.QuizAttemptEntity
) {
    val dateStr = remember(attempt.timestamp) {
        SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(attempt.timestamp))
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = attempt.quizTitle,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (attempt.percentage >= 70) Color(0xFFDCFCE7) else Color(0xFFFEF3C7))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "${attempt.percentage}%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (attempt.percentage >= 70) Color(0xFF166534) else Color(0xFF92400E)
                    )
                }
            }

            Text(
                text = dateStr,
                fontSize = 11.5.sp,
                color = Color(0xFF94A3B8)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Marks: ${String.format("%.2f", attempt.marksScored)} / ${attempt.totalQuestions}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryNavy
                )
                Text(
                    text = "Correct: ${attempt.correctCount}",
                    fontSize = 12.sp,
                    color = CorrectGreen
                )
                Text(
                    text = "Wrong: ${attempt.wrongCount}",
                    fontSize = 12.sp,
                    color = WrongRed
                )
            }
        }
    }
}
