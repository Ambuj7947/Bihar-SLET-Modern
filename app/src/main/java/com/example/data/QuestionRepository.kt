package com.example.data

import com.example.data.dao.QuestionDao
import com.example.data.dao.QuizAttemptDao
import com.example.data.dao.StudyNoteDao
import com.example.data.model.QuestionEntity
import com.example.data.model.QuizAttemptEntity
import com.example.data.model.StudyNoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class QuestionRepository(
    private val questionDao: QuestionDao,
    private val quizAttemptDao: QuizAttemptDao,
    private val studyNoteDao: StudyNoteDao
) {
    val allQuestions: Flow<List<QuestionEntity>> = questionDao.getAllQuestionsFlow()
    val bookmarkedQuestions: Flow<List<QuestionEntity>> = questionDao.getBookmarkedQuestionsFlow()
    val mistakeQuestions: Flow<List<QuestionEntity>> = questionDao.getMistakeQuestionsFlow()
    val categories: Flow<List<String>> = questionDao.getDistinctCategoriesFlow()
    val allStudyNotes: Flow<List<StudyNoteEntity>> = studyNoteDao.getAllNotesFlow()
    val allAttempts: Flow<List<QuizAttemptEntity>> = quizAttemptDao.getAllAttemptsFlow()
    val recentAttempts: Flow<List<QuizAttemptEntity>> = quizAttemptDao.getRecentAttemptsFlow()

    suspend fun initializeAndSeedDatabase() = withContext(Dispatchers.IO) {
        val count = questionDao.getQuestionCount()
        if (count < 330) {
            val initial = DefaultQuestionBank.getAllQuestions()
            questionDao.insertAll(initial)
        }
        val notesCount = studyNoteDao.getNotesCount()
        if (notesCount == 0) {
            studyNoteDao.insertAll(DefaultStudyNotes.allNotes)
        }
    }

    suspend fun getQuestionsByCategory(category: String): List<QuestionEntity> = withContext(Dispatchers.IO) {
        questionDao.getQuestionsByCategory(category)
    }

    suspend fun getRandomQuestions(count: Int): List<QuestionEntity> = withContext(Dispatchers.IO) {
        questionDao.getRandomQuestions(count)
    }

    suspend fun getRandomQuestionsByCategory(category: String, count: Int): List<QuestionEntity> = withContext(Dispatchers.IO) {
        questionDao.getRandomQuestionsByCategory(category, count)
    }

    suspend fun recordQuestionAttempt(id: Long, chosenOption: Int) = withContext(Dispatchers.IO) {
        questionDao.recordAttempt(id, chosenOption)
    }

    suspend fun toggleBookmark(id: Long, currentStatus: Boolean) = withContext(Dispatchers.IO) {
        questionDao.updateBookmark(id, !currentStatus)
    }

    suspend fun saveQuizAttempt(attempt: QuizAttemptEntity): Long = withContext(Dispatchers.IO) {
        quizAttemptDao.insertAttempt(attempt)
    }

    suspend fun resetAllAttempts() = withContext(Dispatchers.IO) {
        questionDao.resetAllAttempts()
        quizAttemptDao.clearHistory()
    }
}
