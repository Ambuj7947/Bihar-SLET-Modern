package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_attempts")
data class QuizAttemptEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val quizTitle: String,
    val categoryName: String, // "ALL" or specific unit
    val testType: String = "MOCK_TEST", // "MOCK_TEST", "TOPIC_TEST", "DAILY_QUIZ", "MISTAKE_REVISION"
    val totalQuestions: Int,
    val correctCount: Int,
    val wrongCount: Int,
    val skippedCount: Int = 0,
    val marksScored: Float = 0f,
    val totalMarks: Float = 0f,
    val timeTakenSeconds: Long = 0L,
    val percentage: Int = if (totalQuestions > 0) (correctCount * 100) / totalQuestions else 0,
    val timestamp: Long = System.currentTimeMillis()
) {
    val accuracyPercentage: Int
        get() {
            val attempted = correctCount + wrongCount
            return if (attempted > 0) (correctCount * 100) / attempted else 0
        }
}
