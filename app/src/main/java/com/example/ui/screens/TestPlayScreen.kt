package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.QuizViewModel
import com.example.ui.components.OptionSelector
import com.example.ui.components.QuestionPaletteBottomSheet
import com.example.ui.components.TestbookTopBar
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CorrectGreen
import com.example.ui.theme.PrimaryNavy
import com.example.ui.theme.ReviewPurple
import com.example.ui.theme.WrongRed

@Composable
fun TestPlayScreen(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier
) {
    val testState by viewModel.testState.collectAsState()

    var showExitDialog by remember { mutableStateOf(false) }
    var showSubmitDialog by remember { mutableStateOf(false) }
    var showPaletteSheet by remember { mutableStateOf(false) }

    BackHandler {
        showExitDialog = true
    }

    // Exit Confirmation Dialog
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = {
                Text("Pause or Exit Test?", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            },
            text = {
                Text(
                    "Are you sure you want to exit? Your answered questions will be saved if you submit the test.",
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                        viewModel.submitTest()
                    }
                ) {
                    Text("Submit & Exit", color = PrimaryNavy, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    viewModel.navigateBack()
                }) {
                    Text("Discard", color = Color(0xFFDC2626))
                }
            }
        )
    }

    // Submit Test Confirmation Dialog
    if (showSubmitDialog) {
        val total = testState.questions.size
        val answered = testState.answeredCount
        val marked = testState.markedForReviewCount
        val notVisited = testState.notVisitedCount
        val notAnswered = testState.notAnsweredCount

        AlertDialog(
            onDismissRequest = { showSubmitDialog = false },
            title = {
                Text("Submit Test Summary", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Are you ready to submit the test?", fontSize = 13.5.sp, color = Color(0xFF64748B))

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Questions:", fontSize = 13.sp)
                        Text("$total", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Answered:", fontSize = 13.sp, color = CorrectGreen)
                        Text("$answered", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = CorrectGreen)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Marked for Review:", fontSize = 13.sp, color = ReviewPurple)
                        Text("$marked", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = ReviewPurple)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Not Answered / Visited:", fontSize = 13.sp, color = WrongRed)
                        Text("${notVisited + notAnswered}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = WrongRed)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSubmitDialog = false
                        viewModel.submitTest()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)
                ) {
                    Text("Yes, Submit Test", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSubmitDialog = false }) {
                    Text("Resume Test")
                }
            }
        )
    }

    // Question Palette Bottom Sheet
    if (showPaletteSheet) {
        QuestionPaletteBottomSheet(
            totalQuestions = testState.questions.size,
            currentQuestionIndex = testState.currentIndex,
            questionStatuses = testState.questions.indices.map { testState.getStatusForQuestion(it) },
            onSelectQuestion = { viewModel.jumpToQuestion(it) },
            onSubmitClick = {
                showPaletteSheet = false
                showSubmitDialog = true
            },
            onDismiss = { showPaletteSheet = false }
        )
    }

    val currentQ = testState.currentQuestion
    val selectedOption = currentQ?.let { testState.userAnswers[it.id] }

    Scaffold(
        topBar = {
            TestbookTopBar(
                testTitle = testState.title,
                remainingSeconds = testState.remainingTimeSeconds,
                onBackClick = { showExitDialog = true },
                onPaletteClick = { showPaletteSheet = true },
                onSubmitClick = { showSubmitDialog = true }
            )
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
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Previous Button
                    OutlinedButton(
                        onClick = { viewModel.previousQuestion() },
                        enabled = testState.currentIndex > 0,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(0.9f)
                            .height(46.dp)
                            .testTag("test_previous_btn")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Previous", fontSize = 12.5.sp)
                    }

                    // Mark for Review & Next (Testbook signature)
                    OutlinedButton(
                        onClick = { viewModel.markForReviewAndNext() },
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ReviewPurple),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ReviewPurple),
                        modifier = Modifier
                            .weight(1.2f)
                            .height(46.dp)
                            .testTag("test_mark_review_btn")
                    ) {
                        Icon(Icons.Default.Flag, contentDescription = null, tint = ReviewPurple, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Mark & Next", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }

                    // Save & Next Button
                    val isLastQuestion = testState.currentIndex == testState.questions.size - 1
                    Button(
                        onClick = {
                            if (isLastQuestion) {
                                showSubmitDialog = true
                            } else {
                                viewModel.saveAndNext()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1.1f)
                            .height(46.dp)
                            .testTag("test_save_next_btn")
                    ) {
                        Text(
                            text = if (isLastQuestion) "Submit" else "Save & Next",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = if (isLastQuestion) Icons.Default.CheckCircle else Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        if (currentQ == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Loading Questions...")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Top Progress Strip
                val progressFraction = (testState.currentIndex + 1).toFloat() / testState.questions.size.coerceAtLeast(1)
                LinearProgressIndicator(
                    progress = { progressFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    color = AmberAccent,
                    trackColor = Color(0xFFE2E8F0)
                )

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Question Meta Strip (Question No, Marks Scheme, Bookmark & Review toggle)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Q. ${testState.currentIndex + 1}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy
                            )
                            Text(
                                text = " of ${testState.questions.size}",
                                fontSize = 13.sp,
                                color = Color(0xFF64748B)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            // Marks pill
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFDCFCE7))
                                    .padding(horizontal = 7.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "+1.00  -0.25",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF166534)
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Bookmark toggle
                            IconButton(
                                onClick = { viewModel.toggleBookmark(currentQ) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (currentQ.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "Bookmark",
                                    tint = if (currentQ.isBookmarked) AmberAccent else Color(0xFF94A3B8)
                                )
                            }

                            // Mark for review toggle
                            IconButton(
                                onClick = { viewModel.toggleMarkForReview() },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (testState.isCurrentMarkedForReview) Icons.Default.Flag else Icons.Outlined.Flag,
                                    contentDescription = "Mark for Review",
                                    tint = if (testState.isCurrentMarkedForReview) ReviewPurple else Color(0xFF94A3B8)
                                )
                            }
                        }
                    }

                    // Section / Category Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFEFF6FF))
                            .padding(horizontal = 9.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = currentQ.category,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1D4ED8)
                        )
                    }

                    // Question Card
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = currentQ.questionHindi,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.5.sp,
                                    lineHeight = 24.sp
                                ),
                                color = Color(0xFF0F172A)
                            )

                            if (currentQ.questionEnglish.isNotBlank()) {
                                Text(
                                    text = currentQ.questionEnglish,
                                    fontSize = 13.5.sp,
                                    color = Color(0xFF475569),
                                    lineHeight = 19.sp
                                )
                            }
                        }
                    }

                    // Options List
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        for (optIndex in 1..4) {
                            val optText = currentQ.getOptionText(optIndex)
                            val isSelected = selectedOption == optIndex

                            OptionSelector(
                                optionIndex = optIndex,
                                optionText = optText,
                                isSelected = isSelected,
                                onClick = { viewModel.selectOption(optIndex) }
                            )
                        }
                    }

                    // Clear response button if answered
                    if (selectedOption != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = { viewModel.clearCurrentResponse() }
                            ) {
                                Text("Clear Response", color = Color(0xFFDC2626), fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }

                    // If in Practice Mode, show instant solution once answered
                    if (testState.isPracticeMode && selectedOption != null) {
                        val isCorrect = selectedOption == currentQ.correctOption
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isCorrect) Color(0xFFF0FDF4) else Color(0xFFFEF2F2)
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isCorrect) Color(0xFF86EFAC) else Color(0xFFFCA5A5)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Lightbulb,
                                        contentDescription = null,
                                        tint = if (isCorrect) CorrectGreen else WrongRed,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isCorrect) "Correct Answer!" else "Explanation & Solution",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.5.sp,
                                        color = if (isCorrect) Color(0xFF14532D) else Color(0xFF991B1B)
                                    )
                                }

                                Text(
                                    text = currentQ.explanation,
                                    fontSize = 13.5.sp,
                                    color = Color(0xFF1E293B),
                                    lineHeight = 20.sp
                                )

                                if (currentQ.keyHighlight.isNotBlank()) {
                                    Text(
                                        text = "Tip: ${currentQ.keyHighlight}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF92400E)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
