package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.DefaultQuestionBank
import com.example.data.DefaultStudyNotes
import com.example.data.model.QuestionEntity
import com.example.data.model.QuizAttemptEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Librarian Exam Prep", appName)
    }

    @Test
    fun `verify all 6 syllabus units are present in question bank`() {
        val allUnits = DefaultQuestionBank.allUnits
        assertEquals(6, allUnits.size)

        val allQuestions = DefaultQuestionBank.getAllQuestions()
        assertEquals("Total extracted questions must match exact count 330", 330, allQuestions.size)

        val u1 = allQuestions.count { it.category == DefaultQuestionBank.UNIT_1 }
        val u2 = allQuestions.count { it.category == DefaultQuestionBank.UNIT_2 }
        val u3 = allQuestions.count { it.category == DefaultQuestionBank.UNIT_3 }
        val u4 = allQuestions.count { it.category == DefaultQuestionBank.UNIT_4 }
        val u5 = allQuestions.count { it.category == DefaultQuestionBank.UNIT_5 }
        val u6 = allQuestions.count { it.category == DefaultQuestionBank.UNIT_6 }

        assertEquals("Unit 1 has 55 questions", 55, u1)
        assertEquals("Unit 2 has 25 questions", 25, u2)
        assertEquals("Unit 3 has 25 questions", 25, u3)
        assertEquals("Unit 4 has 25 questions", 25, u4)
        assertEquals("Unit 5 has 25 questions", 25, u5)
        assertEquals("Unit 6 has 175 questions across 18 sets", 175, u6)
    }

    @Test
    fun `verify study notes cover all core syllabus topics`() {
        val notes = DefaultStudyNotes.allNotes
        assertTrue("Study notes should cover core topics", notes.isNotEmpty())
        notes.forEach { note ->
            assertTrue("Note summary should not be blank", note.summary.isNotBlank())
            assertTrue("Note key facts should not be blank", note.keyFacts.isNotBlank())
        }
    }

    @Test
    fun `verify quiz scoring and negative marking calculations`() {
        val attempt = QuizAttemptEntity(
            quizTitle = "Mock Test #1",
            categoryName = "ALL",
            totalQuestions = 20,
            correctCount = 15,
            wrongCount = 4,
            skippedCount = 1,
            marksScored = (15 * 1.0f) - (4 * 0.25f), // 14.00
            totalMarks = 20.0f
        )

        assertEquals(75, attempt.percentage) // 15/20 = 75%
        assertEquals(14.0f, attempt.marksScored, 0.01f)
        assertEquals(78, attempt.accuracyPercentage) // 15/(15+4) = 78%
    }
}
