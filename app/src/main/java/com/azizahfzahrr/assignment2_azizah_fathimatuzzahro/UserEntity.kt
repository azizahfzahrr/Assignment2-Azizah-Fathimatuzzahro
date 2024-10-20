package com.azizahfzahrr.assignment2_azizah_fathimatuzzahro

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_entity")
data class UserEntity (
    @PrimaryKey(autoGenerate = true) val id: Int= 0,
    @ColumnInfo(name = "nama") val nama: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "password") val password: String,
)