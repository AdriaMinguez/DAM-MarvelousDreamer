package com.example.marvelousdreamer.data.local.dao

import androidx.room.*
import com.example.marvelousdreamer.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: String): UserEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE username = :username AND id != :excludeId)")
    suspend fun isUsernameTaken(username: String, excludeId: String = ""): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Update
    suspend fun update(user: UserEntity)
}
