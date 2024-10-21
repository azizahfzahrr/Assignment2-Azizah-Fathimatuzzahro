package com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DiaryEntity::class], version = 2)
    abstract class DiaryDatabase : RoomDatabase() {

        abstract fun diaryDao(): DiaryDAO

        companion object {
            @Volatile
            private var INSTANCE: DiaryDatabase? = null

            fun getDatabase(context: Context): DiaryDatabase {
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        DiaryDatabase::class.java,
                        "diary_database"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                    instance
                }
            }
        }
    }