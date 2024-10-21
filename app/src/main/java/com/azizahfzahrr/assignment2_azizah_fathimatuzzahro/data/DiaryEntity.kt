package com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diary_entity")
data class DiaryEntity(
    @PrimaryKey(autoGenerate = true)  val id: Int = 0,
    @ColumnInfo(name = "title")  val title: String,
    @ColumnInfo(name = "description")  val description: String,
    @ColumnInfo(name = "date")  val date: Long = System.currentTimeMillis()
)