package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.DefaultQuestionBank
import com.example.data.QuestionRepository
import com.example.data.model.QuestionEntity
import com.example.data.model.QuizAttemptEntity
import com.example.data.model.StudyNoteEntity
import com.example.ui.components.QuestionStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MainTab {
    HOME,
    TEST_SERIES,
    ANALYTICS,
    SAVED,
    NOTES
}

sealed interface AppScreen {
    data class Main(val tab: MainTab = MainTab.HOME) : AppScreen
    data object TestPlay : AppScreen
    data class TestResult(val attempt: QuizAttemptEntity, val questions: List<QuestionEntity>, val answers: Map<Long, Int>) : AppScreen
    data class TopicPractice(val category: String) : AppScreen
}

data class ActiveTestState(
    val title: String = "",
    val categoryName: String = "ALL",
    val testType: String = "MOCK_TEST", // "MOCK_TEST", "TOPIC_TEST", "DAILY_QUIZ", "MISTAKE_REVISION"
    val questions: List<QuestionEntity> = emptyList(),
    val currentIndex: Int = 0,
    val userAnswers: Map<Long, Int> = emptyMap(),
    val markedForReview: Set<Long> = emptySet(),
    val visitedQuestions: Set<Long> = emptySet(),
    val totalTimeSeconds: Long = 1200L,
    val remainingTimeSeconds: Long = 1200L,
    val isPracticeMode: Boolean = false, // If true, shows instant answers/explanations
    val isSubmitted: Boolean = false
) {
    val currentQuestion: QuestionEntity?
        get() = questions.getOrNull(currentIndex)

    val currentAnswer: Int?
        get() = currentQuestion?.let { userAnswers[it.id] }

    val isCurrentMarkedForReview: Boolean
        get() = currentQuestion?.let { markedForReview.contains(it.id) } ?: false

    val answeredCount: Int
        get() = userAnswers.size

    val notAnsweredCount: Int
        get() = visitedQuestions.count { qId -> !userAnswers.containsKey(qId) }

    val markedForReviewCount: Int
        get() = markedForReview.size

    val notVisitedCount: Int
        get() = questions.size - visitedQuestions.size

    val correctCount: Int
        get() = questions.count { q -> userAnswers[q.id] == q.correctOption }

    val wrongCount: Int
        get() = questions.count { q ->
            val ans = userAnswers[q.id]
            ans != null && ans != q.correctOption
        }

    // Marks scored with standard competitive exam rule: +1 for correct, -0.25 for wrong
    val marksScored: Float
        get() = (correctCount * 1.0f) - (wrongCount * 0.25f)

    val totalMarks: Float
        get() = questions.size * 1.0f

    fun getStatusForQuestion(index: Int): QuestionStatus {
        val q = questions.getOrNull(index) ?: return QuestionStatus.NOT_VISITED
        val isVisited = visitedQuestions.contains(q.id)
        val isAns = userAnswers.containsKey(q.id)
        val isMarked = markedForReview.contains(q.id)

        return when {
            isAns && isMarked -> QuestionStatus.ANSWERED_AND_MARKED_FOR_REVIEW
            isMarked -> QuestionStatus.MARKED_FOR_REVIEW
            isAns -> QuestionStatus.ANSWERED
            isVisited -> QuestionStatus.NOT_ANSWERED
            else -> QuestionStatus.NOT_VISITED
        }
    }
}

class QuizViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: QuestionRepository
    private var timerJob: Job? = null

    init {
        val db = AppDatabase.getDatabase(application)
        repository = QuestionRepository(
            db.questionDao(),
            db.quizAttemptDao(),
            db.studyNoteDao()
        )
        viewModelScope.launch {
            repository.initializeAndSeedDatabase()
        }
    }

    // Navigation State
    private val _currentScreen = MutableStateFlow<AppScreen>(AppScreen.Main(MainTab.HOME))
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _currentTab = MutableStateFlow(MainTab.HOME)
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    // Active Test Engine State
    private val _testState = MutableStateFlow(ActiveTestState())
    val testState: StateFlow<ActiveTestState> = _testState.asStateFlow()

    // Data Streams from Room
    val allQuestions: StateFlow<List<QuestionEntity>> = repository.allQuestions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookmarkedQuestions: StateFlow<List<QuestionEntity>> = repository.bookmarkedQuestions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val mistakeQuestions: StateFlow<List<QuestionEntity>> = repository.mistakeQuestions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allStudyNotes: StateFlow<List<StudyNoteEntity>> = repository.allStudyNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentAttempts: StateFlow<List<QuizAttemptEntity>> = repository.recentAttempts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAttempts: StateFlow<List<QuizAttemptEntity>> = repository.allAttempts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectTab(tab: MainTab) {
        _currentTab.value = tab
        _currentScreen.value = AppScreen.Main(tab)
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun navigateBack() {
        if (_currentScreen.value is AppScreen.TestPlay) {
            timerJob?.cancel()
        }
        _currentScreen.value = AppScreen.Main(_currentTab.value)
    }

    // --- TEST ENGINE LOGIC ---

    fun startFullMockTest(testNumber: Int = 1) {
        viewModelScope.launch {
            val list = repository.getRandomQuestions(20)
            val questions = if (list.isNotEmpty()) list else allQuestions.value.take(20)
            startTest(
                title = "Full Mock Test #$testNumber",
                category = "ALL",
                type = "MOCK_TEST",
                questions = questions,
                durationSeconds = 20 * 60L,
                isPractice = false
            )
        }
    }

    fun startSectionalTest(category: String) {
        viewModelScope.launch {
            val list = repository.getQuestionsByCategory(category)
            val questions = if (list.isNotEmpty()) list else allQuestions.value.filter { it.category == category }
            startTest(
                title = category,
                category = category,
                type = "TOPIC_TEST",
                questions = questions,
                durationSeconds = (questions.size * 60).toLong().coerceAtLeast(300L),
                isPractice = false
            )
        }
    }

    fun startDailyQuiz() {
        viewModelScope.launch {
            val list = repository.getRandomQuestions(10)
            val questions = if (list.isNotEmpty()) list else allQuestions.value.take(10)
            startTest(
                title = "Daily Target Quiz",
                category = "ALL",
                type = "DAILY_QUIZ",
                questions = questions,
                durationSeconds = 10 * 60L,
                isPractice = false
            )
        }
    }

    fun startTopicPracticeMode(category: String) {
        viewModelScope.launch {
            val list = repository.getQuestionsByCategory(category)
            val questions = if (list.isNotEmpty()) list else allQuestions.value.filter { it.category == category }
            startTest(
                title = "Practice: $category",
                category = category,
                type = "PRACTICE",
                questions = questions,
                durationSeconds = 0L,
                isPractice = true
            )
        }
    }

    fun startMistakeRevision() {
        val mistakes = mistakeQuestions.value
        if (mistakes.isEmpty()) return
        startTest(
            title = "Mistake Notebook Revision",
            category = "MISTAKES",
            type = "MISTAKE_REVISION",
            questions = mistakes,
            durationSeconds = (mistakes.size * 60).toLong().coerceAtLeast(300L),
            isPractice = false
        )
    }

    fun startBookmarkedQuiz() {
        val bookmarks = bookmarkedQuestions.value
        if (bookmarks.isEmpty()) return
        startTest(
            title = "Bookmarked Questions Test",
            category = "BOOKMARKS",
            type = "BOOKMARKED",
            questions = bookmarks,
            durationSeconds = (bookmarks.size * 60).toLong().coerceAtLeast(300L),
            isPractice = false
        )
    }

    private fun startTest(
        title: String,
        category: String,
        type: String,
        questions: List<QuestionEntity>,
        durationSeconds: Long,
        isPractice: Boolean
    ) {
        timerJob?.cancel()

        val initialVisited = if (questions.isNotEmpty()) setOf(questions.first().id) else emptySet()

        _testState.value = ActiveTestState(
            title = title,
            categoryName = category,
            testType = type,
            questions = questions,
            currentIndex = 0,
            userAnswers = emptyMap(),
            markedForReview = emptySet(),
            visitedQuestions = initialVisited,
            totalTimeSeconds = durationSeconds,
            remainingTimeSeconds = durationSeconds,
            isPracticeMode = isPractice,
            isSubmitted = false
        )

        _currentScreen.value = AppScreen.TestPlay

        // Start countdown timer if in timed mode
        if (!isPractice && durationSeconds > 0) {
            timerJob = viewModelScope.launch {
                while (_testState.value.remainingTimeSeconds > 0 && !_testState.value.isSubmitted) {
                    delay(1000)
                    _testState.value = _testState.value.copy(
                        remainingTimeSeconds = _testState.value.remainingTimeSeconds - 1
                    )
                }
                if (!_testState.value.isSubmitted) {
                    submitTest()
                }
            }
        }
    }

    fun selectOption(optionIndex: Int) {
        val currentQ = _testState.value.currentQuestion ?: return
        val updatedAnswers = _testState.value.userAnswers.toMutableMap()
        updatedAnswers[currentQ.id] = optionIndex

        _testState.value = _testState.value.copy(userAnswers = updatedAnswers)

        // In practice mode, record immediately for instant feedback
        if (_testState.value.isPracticeMode) {
            viewModelScope.launch {
                repository.recordQuestionAttempt(currentQ.id, optionIndex)
            }
        }
    }

    fun clearCurrentResponse() {
        val currentQ = _testState.value.currentQuestion ?: return
        val updatedAnswers = _testState.value.userAnswers.toMutableMap()
        updatedAnswers.remove(currentQ.id)

        _testState.value = _testState.value.copy(userAnswers = updatedAnswers)
    }

    fun toggleMarkForReview() {
        val currentQ = _testState.value.currentQuestion ?: return
        val currentMarked = _testState.value.markedForReview.toMutableSet()
        if (currentMarked.contains(currentQ.id)) {
            currentMarked.remove(currentQ.id)
        } else {
            currentMarked.add(currentQ.id)
        }
        _testState.value = _testState.value.copy(markedForReview = currentMarked)
    }

    fun markForReviewAndNext() {
        val currentQ = _testState.value.currentQuestion ?: return
        val currentMarked = _testState.value.markedForReview.toMutableSet()
        currentMarked.add(currentQ.id)

        _testState.value = _testState.value.copy(markedForReview = currentMarked)
        nextQuestion()
    }

    fun saveAndNext() {
        nextQuestion()
    }

    fun nextQuestion() {
        val state = _testState.value
        if (state.currentIndex < state.questions.size - 1) {
            val nextIndex = state.currentIndex + 1
            val nextQ = state.questions[nextIndex]
            val updatedVisited = state.visitedQuestions + nextQ.id
            _testState.value = state.copy(
                currentIndex = nextIndex,
                visitedQuestions = updatedVisited
            )
        }
    }

    fun previousQuestion() {
        val state = _testState.value
        if (state.currentIndex > 0) {
            val prevIndex = state.currentIndex - 1
            val prevQ = state.questions[prevIndex]
            val updatedVisited = state.visitedQuestions + prevQ.id
            _testState.value = state.copy(
                currentIndex = prevIndex,
                visitedQuestions = updatedVisited
            )
        }
    }

    fun jumpToQuestion(index: Int) {
        val state = _testState.value
        if (index in state.questions.indices) {
            val targetQ = state.questions[index]
            val updatedVisited = state.visitedQuestions + targetQ.id
            _testState.value = state.copy(
                currentIndex = index,
                visitedQuestions = updatedVisited
            )
        }
    }

    fun submitTest() {
        timerJob?.cancel()
        val state = _testState.value
        if (state.isSubmitted) return

        val totalTimeTaken = state.totalTimeSeconds - state.remainingTimeSeconds

        val attempt = QuizAttemptEntity(
            quizTitle = state.title,
            categoryName = state.categoryName,
            testType = state.testType,
            totalQuestions = state.questions.size,
            correctCount = state.correctCount,
            wrongCount = state.wrongCount,
            skippedCount = state.questions.size - state.userAnswers.size,
            marksScored = state.marksScored,
            totalMarks = state.totalMarks,
            timeTakenSeconds = totalTimeTaken.coerceAtLeast(1L),
            timestamp = System.currentTimeMillis()
        )

        viewModelScope.launch {
            // Save attempt to Room
            repository.saveQuizAttempt(attempt)

            // Record each question attempt in Room
            state.userAnswers.forEach { (qId, chosenOpt) ->
                repository.recordQuestionAttempt(qId, chosenOpt)
            }
        }

        _testState.value = state.copy(isSubmitted = true)
        _currentScreen.value = AppScreen.TestResult(
            attempt = attempt,
            questions = state.questions,
            answers = state.userAnswers
        )
    }

    fun toggleBookmark(question: QuestionEntity) {
        viewModelScope.launch {
            repository.toggleBookmark(question.id, question.isBookmarked)
        }
    }

    fun retryMistakesFromActiveTest() {
        val state = _testState.value
        val mistakes = state.questions.filter { q ->
            val ans = state.userAnswers[q.id]
            ans != null && ans != q.correctOption
        }
        if (mistakes.isNotEmpty()) {
            startTest(
                title = "Re-attempt Mistakes (${state.title})",
                category = state.categoryName,
                type = "MISTAKE_REVISION",
                questions = mistakes,
                durationSeconds = (mistakes.size * 60).toLong(),
                isPractice = false
            )
        }
    }

    fun reAttemptCurrentTest() {
        val state = _testState.value
        startTest(
            title = state.title,
            category = state.categoryName,
            type = state.testType,
            questions = state.questions,
            durationSeconds = state.totalTimeSeconds,
            isPractice = state.isPracticeMode
        )
    }
}
