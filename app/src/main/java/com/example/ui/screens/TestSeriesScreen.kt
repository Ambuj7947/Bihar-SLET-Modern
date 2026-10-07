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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.data.DefaultQuestionBank
import com.example.ui.QuizViewModel
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CorrectGreen
import com.example.ui.theme.PrimaryNavy

@Composable
fun TestSeriesScreen(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier
) {
    var selectedCategoryTab by remember { mutableIntStateOf(0) } // 0 = Full Mocks, 1 = Sectional Tests, 2 = PYQ Sets
    val attempts by viewModel.allAttempts.collectAsState()

    val tabTitles = listOf("Full Mocks", "Sectional Tests", "PYQ Sets")

    Column(modifier = modifier.fillMaxSize()) {
        // Tab Row
        TabRow(
            selectedTabIndex = selectedCategoryTab,
            containerColor = Color.White,
            contentColor = PrimaryNavy,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedCategoryTab == index,
                    onClick = { selectedCategoryTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedCategoryTab == index) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.5.sp
                        )
                    },
                    modifier = Modifier.testTag("test_series_tab_$index")
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            when (selectedCategoryTab) {
                0 -> {
                    // Full Mock Tests (Mock #1, Mock #2, Mock #3)
                    val mockTests = listOf(
                        Triple(1, "BLET / KVS Full Mock Test #1", 20),
                        Triple(2, "Library Science Full Mock Test #2", 20),
                        Triple(3, "All India Librarian Live Mock #3", 20)
                    )

                    itemsIndexed(mockTests) { _, item ->
                        val (number, title, qCount) = item
                        val pastAttempt = attempts.find { it.quizTitle.contains("#$number") }

                        TestCatalogCard(
                            title = title,
                            badge = "FULL LENGTH",
                            totalQuestions = qCount,
                            durationMinutes = 20,
                            pastScore = pastAttempt?.percentage,
                            onStartTest = { viewModel.startFullMockTest(number) },
                            modifier = Modifier.testTag("mock_test_card_$number")
                        )
                    }
                }
                1 -> {
                    // Sectional Tests (Units 1 to 5)
                    val sectionalUnits = DefaultQuestionBank.allUnits.take(5)
                    itemsIndexed(sectionalUnits) { index, unitTitle ->
                        val pastAttempt = attempts.find { it.quizTitle == unitTitle }

                        TestCatalogCard(
                            title = unitTitle,
                            badge = "SECTIONAL • UNIT 0${index + 1}",
                            totalQuestions = 10,
                            durationMinutes = 10,
                            pastScore = pastAttempt?.percentage,
                            onStartTest = { viewModel.startSectionalTest(unitTitle) },
                            modifier = Modifier.testTag("sectional_test_card_$index")
                        )
                    }
                }
                2 -> {
                    // All 18 Practice Sets from Unit 6
                    val setsList = listOf(
                        Pair(1, "अभ्यास सेट 01: पुस्तकालय की अवधारणा व NAPLIS (10 MCQs)"),
                        Pair(2, "अभ्यास सेट 02: पुस्तकालय विज्ञान के 5 सूत्र (10 MCQs)"),
                        Pair(3, "अभ्यास सेट 03: सार्वजनिक पुस्तकालय व RRRLF (10 MCQs)"),
                        Pair(4, "अभ्यास सेट 04: UNESCO, IFLA, UGC व आयोग (10 MCQs)"),
                        Pair(5, "अभ्यास सेट 05: विशिष्ट पुस्तकालय: CAS, SDI, DESIDOC (10 MCQs)"),
                        Pair(6, "अभ्यास सेट 06: शैक्षणिक व राष्ट्रीय पुस्तकालय (10 MCQs)"),
                        Pair(7, "अभ्यास सेट 07: डॉ. एस. आर. रंगनाथन विशेष (10 MCQs)"),
                        Pair(8, "अभ्यास सेट 08: भारतीय राष्ट्रीय पुस्तकालय व INB (10 MCQs)"),
                        Pair(9, "अभ्यास सेट 09: DDC, CC, UDC वर्गीकरण प्रणालियां (10 MCQs)"),
                        Pair(10, "अभ्यास सेट 10: AACR-2, CCC, MARC 21 व Dublin Core (10 MCQs)"),
                        Pair(11, "अभ्यास सेट 11: POSDCORB, टेलर, फेयोल व बजट (10 MCQs)"),
                        Pair(12, "अभ्यास सेट 12: पुस्तक चयन, ब्राउन, नेवार्क चार्जिंग (10 MCQs)"),
                        Pair(13, "अभ्यास सेट 13: संदर्भ सेवा, KWIC, PRECIS, POPSI (10 MCQs)"),
                        Pair(14, "अभ्यास सेट 14: कोहा, SOUL, DSpace, RFID व NDLI (10 MCQs)"),
                        Pair(15, "अभ्यास सेट 15: बिहार पुस्तकालय, नालंदा, खुदा बख्श (10 MCQs)"),
                        Pair(16, "अभ्यास सेट 16: विविध अभ्यास पेपर भाग-1 (10 MCQs)"),
                        Pair(17, "अभ्यास सेट 17: विविध अभ्यास पेपर भाग-2 (10 MCQs)"),
                        Pair(18, "अभ्यास सेट 18: विविध अभ्यास पेपर भाग-3 (5 MCQs)")
                    )

                    itemsIndexed(setsList) { _, (setNum, setTitle) ->
                        val pastAttempt = attempts.find { it.quizTitle.contains("सेट ${String.format("%02d", setNum)}") || it.quizTitle.contains("Set $setNum") }

                        TestCatalogCard(
                            title = setTitle,
                            badge = "PRACTICE SET ${String.format("%02d", setNum)}",
                            totalQuestions = if (setNum == 18) 5 else 10,
                            durationMinutes = if (setNum == 18) 6 else 10,
                            pastScore = pastAttempt?.percentage,
                            onStartTest = {
                                val setQuestions = when (setNum) {
                                    1 -> com.example.data.Unit6Questions.getSet1Questions()
                                    2 -> com.example.data.Unit6Questions.getSet2Questions()
                                    3 -> com.example.data.Unit6Questions.getSet3Questions()
                                    4 -> com.example.data.Unit6Questions.getSet4Questions()
                                    5 -> com.example.data.Unit6Questions.getSet5Questions()
                                    6 -> com.example.data.Unit6Questions.getSet6Questions()
                                    7 -> com.example.data.Unit6Questions.getSet7Questions()
                                    8 -> com.example.data.Unit6Questions.getSet8Questions()
                                    9 -> com.example.data.Unit6Questions.getSet9Questions()
                                    10 -> com.example.data.Unit6Questions.getSet10Questions()
                                    11 -> com.example.data.Unit6Questions.getSet11Questions()
                                    12 -> com.example.data.Unit6Questions.getSet12Questions()
                                    13 -> com.example.data.Unit6Questions.getSet13Questions()
                                    14 -> com.example.data.Unit6Questions.getSet14Questions()
                                    15 -> com.example.data.Unit6Questions.getSet15Questions()
                                    16 -> com.example.data.Unit6Questions.getSet16Questions()
                                    17 -> com.example.data.Unit6Questions.getSet17Questions()
                                    else -> com.example.data.Unit6Questions.getSet18Questions()
                                }
                                viewModel.startTopicPracticeMode(setTitle)
                            },
                            modifier = Modifier.testTag("practice_set_card_$setNum")
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
fun TestCatalogCard(
    title: String,
    badge: String,
    totalQuestions: Int,
    durationMinutes: Int,
    pastScore: Int?,
    onStartTest: () -> Unit,
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
                        text = badge,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1D4ED8)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFFEF3C7))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "FREE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF92400E)
                    )
                }
            }

            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                lineHeight = 22.sp
            )

            // Exam specs strip
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "$totalQuestions Questions",
                    fontSize = 12.5.sp,
                    color = Color(0xFF64748B)
                )
                Text(
                    text = "$totalQuestions Marks",
                    fontSize = 12.5.sp,
                    color = Color(0xFF64748B)
                )
                Text(
                    text = "$durationMinutes Mins",
                    fontSize = 12.5.sp,
                    color = Color(0xFF64748B)
                )
            }

            // Past attempt badge if exists
            if (pastScore != null) {
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
                        text = "Last Attempt Score:",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                    Text(
                        text = "$pastScore%",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (pastScore >= 70) CorrectGreen else AmberAccent
                    )
                }
            }

            // CTA Button
            Button(
                onClick = onStartTest,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (pastScore != null) Color(0xFF1E293B) else PrimaryNavy
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
            ) {
                Icon(
                    imageVector = if (pastScore != null) Icons.Default.Replay else Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (pastScore != null) "Re-attempt Test" else "Start Test Now",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp
                )
            }
        }
    }
}
