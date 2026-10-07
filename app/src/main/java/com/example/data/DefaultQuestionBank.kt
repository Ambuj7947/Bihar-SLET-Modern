package com.example.data

import com.example.data.model.QuestionEntity

object DefaultQuestionBank {

    val UNIT_1 = Unit1Questions.UNIT_1
    val UNIT_2 = UnitsContentData.UNIT_2
    val UNIT_3 = UnitsContentData.UNIT_3
    val UNIT_4 = UnitsContentData.UNIT_4
    val UNIT_5 = UnitsContentData.UNIT_5
    val UNIT_6 = Unit6Questions.UNIT_6

    val allUnits = listOf(UNIT_1, UNIT_2, UNIT_3, UNIT_4, UNIT_5, UNIT_6)

    fun getUnit1Questions(): List<QuestionEntity> = Unit1Questions.getQuestions()
    fun getUnit2Questions(): List<QuestionEntity> = UnitsContentData.getUnit2Questions()
    fun getUnit3Questions(): List<QuestionEntity> = UnitsContentData.getUnit3Questions()
    fun getUnit4Questions(): List<QuestionEntity> = UnitsContentData.getUnit4Questions()
    fun getUnit5Questions(): List<QuestionEntity> = UnitsContentData.getUnit5Questions()
    fun getUnit6Questions(): List<QuestionEntity> = Unit6Questions.getAllQuestions()

    /**
     * All 330 original questions extracted and restored:
     * Unit 1 (55 Qs) + Units 2-5 (100 Qs) + Unit 6 (175 Qs) = 330 questions
     */
    fun getAllQuestions(): List<QuestionEntity> {
        return getUnit1Questions() +
                getUnit2Questions() +
                getUnit3Questions() +
                getUnit4Questions() +
                getUnit5Questions() +
                getUnit6Questions()
    }
}
