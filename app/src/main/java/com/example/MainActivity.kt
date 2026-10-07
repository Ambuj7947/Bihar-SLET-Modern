package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppScreen
import com.example.ui.MainTab
import com.example.ui.QuizViewModel
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SavedMistakesScreen
import com.example.ui.screens.StudyNotesScreen
import com.example.ui.screens.TestPlayScreen
import com.example.ui.screens.TestResultScreen
import com.example.ui.screens.TestSeriesScreen
import com.example.ui.theme.LibrarianTheme
import com.example.ui.theme.PrimaryNavy

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LibrarianTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: QuizViewModel = viewModel()
                    MainAppContent(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: QuizViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentTab by viewModel.currentTab.collectAsState()

    BackHandler(enabled = currentScreen !is AppScreen.Main || currentTab != MainTab.HOME) {
        if (currentScreen is AppScreen.Main && currentTab != MainTab.HOME) {
            viewModel.selectTab(MainTab.HOME)
        } else {
            viewModel.navigateBack()
        }
    }

    when (val screen = currentScreen) {
        is AppScreen.TestPlay -> {
            TestPlayScreen(viewModel = viewModel)
        }
        is AppScreen.TestResult -> {
            TestResultScreen(
                viewModel = viewModel,
                attempt = screen.attempt,
                questions = screen.questions,
                answers = screen.answers
            )
        }
        is AppScreen.TopicPractice -> {
            TestPlayScreen(viewModel = viewModel)
        }
        is AppScreen.Main -> {
            Scaffold(
                topBar = {
                    MainTopAppBar(selectedTab = currentTab)
                },
                bottomBar = {
                    MainBottomNavigationBar(
                        selectedTab = currentTab,
                        onTabSelected = { viewModel.selectTab(it) }
                    )
                },
                modifier = Modifier.fillMaxSize()
            ) { innerPadding ->
                AnimatedContent(
                    targetState = screen.tab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "TabTransition",
                    modifier = Modifier.padding(innerPadding)
                ) { tab ->
                    when (tab) {
                        MainTab.HOME -> HomeScreen(viewModel = viewModel)
                        MainTab.TEST_SERIES -> TestSeriesScreen(viewModel = viewModel)
                        MainTab.ANALYTICS -> AnalyticsScreen(viewModel = viewModel)
                        MainTab.SAVED -> SavedMistakesScreen(viewModel = viewModel)
                        MainTab.NOTES -> StudyNotesScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun MainTopAppBar(selectedTab: MainTab) {
    val (title, subtitle) = when (selectedTab) {
        MainTab.HOME -> Pair("Librarian Exam Prep", "Testbook Practice & Mock Series")
        MainTab.TEST_SERIES -> Pair("Test Series & Mocks", "Full Length & Sectional Tests")
        MainTab.ANALYTICS -> Pair("Performance Analytics", "Scorecard, Accuracy & Ranks")
        MainTab.SAVED -> Pair("Mistakes & Bookmarks", "Targeted Revision Notebook")
        MainTab.NOTES -> Pair("Study Notes", "Syllabus Theory & Quick Facts")
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryNavy)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalLibrary,
                    contentDescription = null,
                    tint = Color(0xFFFBBF24),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.5.sp
                    ),
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    fontSize = 11.5.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }
    }
}

@Composable
fun MainBottomNavigationBar(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .testTag("main_bottom_nav")
    ) {
        NavigationBarItem(
            selected = selectedTab == MainTab.HOME,
            onClick = { onTabSelected(MainTab.HOME) },
            icon = {
                Icon(
                    imageVector = if (selectedTab == MainTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryNavy,
                selectedTextColor = PrimaryNavy,
                indicatorColor = Color(0xFFE0ECFD)
            ),
            modifier = Modifier.testTag("nav_item_home")
        )

        NavigationBarItem(
            selected = selectedTab == MainTab.TEST_SERIES,
            onClick = { onTabSelected(MainTab.TEST_SERIES) },
            icon = {
                Icon(
                    imageVector = if (selectedTab == MainTab.TEST_SERIES) Icons.Filled.Quiz else Icons.Outlined.Quiz,
                    contentDescription = "Tests"
                )
            },
            label = { Text("Tests", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryNavy,
                selectedTextColor = PrimaryNavy,
                indicatorColor = Color(0xFFE0ECFD)
            ),
            modifier = Modifier.testTag("nav_item_tests")
        )

        NavigationBarItem(
            selected = selectedTab == MainTab.ANALYTICS,
            onClick = { onTabSelected(MainTab.ANALYTICS) },
            icon = {
                Icon(
                    imageVector = if (selectedTab == MainTab.ANALYTICS) Icons.Filled.BarChart else Icons.Outlined.BarChart,
                    contentDescription = "Analytics"
                )
            },
            label = { Text("Analytics", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryNavy,
                selectedTextColor = PrimaryNavy,
                indicatorColor = Color(0xFFE0ECFD)
            ),
            modifier = Modifier.testTag("nav_item_analytics")
        )

        NavigationBarItem(
            selected = selectedTab == MainTab.SAVED,
            onClick = { onTabSelected(MainTab.SAVED) },
            icon = {
                Icon(
                    imageVector = if (selectedTab == MainTab.SAVED) Icons.Filled.Bookmark else Icons.Outlined.Bookmark,
                    contentDescription = "Mistakes"
                )
            },
            label = { Text("Mistakes", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryNavy,
                selectedTextColor = PrimaryNavy,
                indicatorColor = Color(0xFFE0ECFD)
            ),
            modifier = Modifier.testTag("nav_item_mistakes")
        )

        NavigationBarItem(
            selected = selectedTab == MainTab.NOTES,
            onClick = { onTabSelected(MainTab.NOTES) },
            icon = {
                Icon(
                    imageVector = if (selectedTab == MainTab.NOTES) Icons.Filled.AutoStories else Icons.Outlined.AutoStories,
                    contentDescription = "Notes"
                )
            },
            label = { Text("Notes", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryNavy,
                selectedTextColor = PrimaryNavy,
                indicatorColor = Color(0xFFE0ECFD)
            ),
            modifier = Modifier.testTag("nav_item_notes")
        )
    }
}
