package com.azizahfzahrr.assignment2_azizah_fathimatuzzahro

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM user_entity")
    suspend fun getUser(): List<UserEntity>

    @Query("SELECT * FROM user_entity WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?
}