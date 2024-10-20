package com.azizahfzahrr.assignment2_azizah_fathimatuzzahro

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface DiaryDAO {

    @Insert
    suspend fun insertDiary(diary: DiaryEntity)

    @Query("SELECT * FROM diary_entity")
    suspend fun getDiary(): List<DiaryEntity>

    @Update
    suspend fun updateDiary(diary: DiaryEntity)

    @Delete
    suspend fun deleteDiary(diary: DiaryEntity)

    @Query("SELECT * FROM diary_entity WHERE title LIKE :title")
    suspend fun searchByTitle(title: String): List<DiaryEntity>

    @Query("SELECT * FROM diary_entity ORDER BY title ASC")
    suspend fun getDiariesSortedByTitle(): List<DiaryEntity>
}
