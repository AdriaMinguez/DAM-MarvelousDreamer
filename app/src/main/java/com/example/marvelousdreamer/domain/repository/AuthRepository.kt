package com.example.marvelousdreamer.domain.repository

import com.example.marvelousdreamer.data.local.entity.UserEntity
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val isLoggedIn: Boolean
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Result<FirebaseUser>
    suspend fun register(email: String, password: String): Result<FirebaseUser>
    suspend fun sendPasswordReset(email: String): Result<Unit>
    suspend fun logout()
    suspend fun getLocalUser(uid: String): UserEntity?
    suspend fun saveLocalUser(user: UserEntity)
    suspend fun isUsernameTaken(username: String, excludeId: String = ""): Boolean
}