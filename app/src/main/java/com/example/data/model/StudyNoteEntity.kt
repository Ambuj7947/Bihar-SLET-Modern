package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_notes")
data class StudyNoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val unitCategory: String,
    val title: String,
    val summary: String,
    val keyFacts: String, // newline-separated bullet points
    val tags: String = "",
    val readTimeMinutes: Int = 5
)
