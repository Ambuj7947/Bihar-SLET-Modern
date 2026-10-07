package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.model.QuizAttemptEntity
import com.example.ui.MainTab
import com.example.ui.QuizViewModel
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CorrectGreen
import com.example.ui.theme.CorrectGreenBg
import com.example.ui.theme.PrimaryNavy
import com.example.ui.theme.WrongRed
import com.example.ui.theme.WrongRedBg

@Composable
fun TestResultScreen(
    viewModel: QuizViewModel,
    attempt: QuizAttemptEntity,
    questions: List<QuestionEntity>,
    answers: Map<Long, Int>,
    modifier: Modifier = Modifier
) {
    var solutionFilter by remember { mutableIntStateOf(0) } // 0 = All, 1 = Incorrect, 2 = Correct, 3 = Skipped

    BackHandler {
        viewModel.navigateBack()
    }

    val minutes = attempt.timeTakenSeconds / 60
    val seconds = attempt.timeTakenSeconds % 60
    val timeFormatted = String.format("%02dm %02ds", minutes, seconds)

    val filteredQuestions = remember(questions, answers, solutionFilter) {
        when (solutionFilter) {
            1 -> questions.filter { q ->
                val ans = answers[q.id]
                ans != null && ans != q.correctOption
            }
            2 -> questions.filter { q -> answers[q.id] == q.correctOption }
            3 -> questions.filter { q -> !answers.containsKey(q.id) }
            else -> questions
        }
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PrimaryNavy)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Column {
                    Text(
                        text = "Scorecard & Analysis",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        color = Color.White
                    )
                    Text(
                        text = "${attempt.quizTitle} • $timeFormatted",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        },
        bottomBar = {
            Surface(
                tonalElevation = 6.dp,
                shadowElevation = 8.dp,
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.navigateBack() },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("result_home_btn")
                    ) {
                        Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Home", fontWeight = FontWeight.Bold)
                    }

                    if (attempt.wrongCount > 0) {
                        Button(
                            onClick = { viewModel.retryMistakesFromActiveTest() },
                            colors = ButtonDefaults.buttonColors(containerColor = WrongRed),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1.3f)
                                .height(46.dp)
                                .testTag("retry_mistakes_test_btn")
                        ) {
                            Icon(Icons.Default.Replay, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Retry Mistakes (${attempt.wrongCount})", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    } else {
                        Button(
                            onClick = { viewModel.reAttemptCurrentTest() },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1.3f)
                                .height(46.dp)
                                .testTag("reattempt_full_test_btn")
                        ) {
                            Icon(Icons.Default.Replay, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Re-attempt Test", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Testbook Scorecard Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                        .testTag("result_scorecard_card")
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Marks Ring
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .clip(CircleShape)
                                .background(if (attempt.percentage >= 60) CorrectGreen.copy(alpha = 0.12f) else WrongRed.copy(alpha = 0.12f))
                                .border(3.dp, if (attempt.percentage >= 60) CorrectGreen else WrongRed, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = String.format("%.2f", attempt.marksScored),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp,
                                    color = if (attempt.percentage >= 60) CorrectGreen else WrongRed
                                )
                                Text(
                                    text = "/ ${attempt.totalQuestions}",
                                    fontSize = 11.5.sp,
                                    color = Color(0xFF64748B),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = when {
                                    attempt.percentage >= 80 -> "Excellent Performance!"
                                    attempt.percentage >= 60 -> "Good Effort! Keep practicing."
                                    else -> "Needs Revision & Practice"
                                },
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "Accuracy: ${attempt.accuracyPercentage}% • Score: ${attempt.percentage}%",
                                fontSize = 13.sp,
                                color = Color(0xFF64748B)
                            )
                        }

                        // Detailed Counters Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ScoreStatBadge(label = "Correct", count = "${attempt.correctCount}", color = CorrectGreen, bg = CorrectGreenBg)
                            ScoreStatBadge(label = "Incorrect", count = "${attempt.wrongCount}", color = WrongRed, bg = WrongRedBg)
                            ScoreStatBadge(label = "Skipped", count = "${attempt.skippedCount}", color = Color(0xFF64748B), bg = Color(0xFFF1F5F9))
                        }
                    }
                }
            }

            // Solution Review Filter Chips
            item {
                Text(
                    text = "Question-by-Question Solution",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    color = Color(0xFF0F172A)
                )
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val filterOptions = listOf(
                        "All (${questions.size})",
                        "Incorrect (${attempt.wrongCount})",
                        "Correct (${attempt.correctCount})",
                        "Skipped (${attempt.skippedCount})"
                    )

                    items(filterOptions.size) { index ->
                        FilterChip(
                            selected = solutionFilter == index,
                            onClick = { solutionFilter = index },
                            label = { Text(filterOptions[index], fontSize = 12.5.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryNavy,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // Question Items with Solution
            itemsIndexed(filteredQuestions) { index, question ->
                val chosenOpt = answers[question.id]
                val isCorrect = chosenOpt == question.correctOption
                val isSkipped = chosenOpt == null

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            if (isCorrect) CorrectGreen.copy(alpha = 0.5f) else if (!isSkipped) WrongRed.copy(alpha = 0.5f) else Color(0xFFE2E8F0),
                            RoundedCornerShape(14.dp)
                        )
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
                                text = "Q. ${index + 1} (${question.category})",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy
                            )

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        when {
                                            isCorrect -> CorrectGreenBg
                                            !isSkipped -> WrongRedBg
                                            else -> Color(0xFFF1F5F9)
                                        }
                                    )
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = when {
                                        isCorrect -> "+1.00 Correct"
                                        !isSkipped -> "-0.25 Wrong"
                                        else -> "0.00 Skipped"
                                    },
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when {
                                        isCorrect -> Color(0xFF15803D)
                                        !isSkipped -> Color(0xFFB91C1C)
                                        else -> Color(0xFF64748B)
                                    }
                                )
                            }
                        }

                        Text(
                            text = question.questionHindi,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            lineHeight = 22.sp
                        )

                        if (question.questionEnglish.isNotBlank()) {
                            Text(
                                text = question.questionEnglish,
                                fontSize = 12.5.sp,
                                color = Color(0xFF475569)
                            )
                        }

                        // Options display
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            for (optIdx in 1..4) {
                                val optText = question.getOptionText(optIdx)
                                val isThisCorrect = optIdx == question.correctOption
                                val isUserChoice = chosenOpt == optIdx

                                val (optBg, optBorder, optTextColor) = when {
                                    isThisCorrect -> Triple(CorrectGreenBg, CorrectGreen, Color(0xFF14532D))
                                    isUserChoice -> Triple(WrongRedBg, WrongRed, Color(0xFF7F1D1D))
                                    else -> Triple(Color(0xFFF8FAFC), Color(0xFFE2E8F0), Color(0xFF475569))
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(optBg)
                                        .border(1.dp, optBorder, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${('A'.code + optIdx - 1).toChar()}.",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = optTextColor
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = optText,
                                        fontSize = 13.sp,
                                        color = optTextColor,
                                        fontWeight = if (isThisCorrect || isUserChoice) FontWeight.Bold else FontWeight.Normal,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }

                        // Explanation Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFEFF6FF))
                                .border(1.dp, Color(0xFFBFDBFE), RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                Text(
                                    text = "Explanation:",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1D4ED8)
                                )
                                Text(
                                    text = question.explanation,
                                    fontSize = 12.5.sp,
                                    color = Color(0xFF1E293B),
                                    lineHeight = 18.sp
                                )

                                if (question.keyHighlight.isNotBlank()) {
                                    Text(
                                        text = "Tip: ${question.keyHighlight}",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFFB45309)
                                    )
                                }
                            }
                        }
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
private fun ScoreStatBadge(
    label: String,
    count: String,
    color: Color,
    bg: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bg)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = count,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = color
            )
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = color.copy(alpha = 0.9f)
            )
        }
    }
}
