package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.StudyNoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyNoteDao {
    @Query("SELECT * FROM study_notes ORDER BY id ASC")
    fun getAllNotesFlow(): Flow<List<StudyNoteEntity>>

    @Query("SELECT * FROM study_notes WHERE unitCategory = :unit ORDER BY id ASC")
    fun getNotesByUnitFlow(unit: String): Flow<List<StudyNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notes: List<StudyNoteEntity>)

    @Query("SELECT COUNT(*) FROM study_notes")
    suspend fun getNotesCount(): Int
}
