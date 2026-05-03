package com.example.marvelousdreamer.data.repository

import android.util.Log
import com.example.marvelousdreamer.data.local.dao.AccessLogDao
import com.example.marvelousdreamer.data.local.dao.UserDao
import com.example.marvelousdreamer.data.local.entity.AccessLogEntity
import com.example.marvelousdreamer.data.local.entity.UserEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Handles Firebase Authentication + local user persistence (T2, T3, T4).
 */
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val userDao: UserDao,
    private val accessLogDao: AccessLogDao
) {
    companion object { private const val TAG = "AuthRepository" }

    val currentUser: FirebaseUser? get() = auth.currentUser

    val isLoggedIn: Boolean get() = auth.currentUser != null

    suspend fun login(email: String, password: String): Result<FirebaseUser> = try {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val user = result.user!!
        logAccess(user.uid, "LOGIN")
        Log.i(TAG, "login: success for ${user.email}")
        Result.success(user)
    } catch (e: Exception) {
        Log.e(TAG, "login: failed - ${e.message}")
        Result.failure(e)
    }

    suspend fun register(email: String, password: String): Result<FirebaseUser> = try {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user!!
        user.sendEmailVerification().await()
        // Create local user profile
        userDao.insert(UserEntity(id = user.uid, login = email, username = email.substringBefore("@")))
        logAccess(user.uid, "LOGIN")
        Log.i(TAG, "register: success for ${user.email}, verification email sent")
        Result.success(user)
    } catch (e: Exception) {
        Log.e(TAG, "register: failed - ${e.message}")
        Result.failure(e)
    }

    suspend fun sendPasswordReset(email: String): Result<Unit> = try {
        auth.sendPasswordResetEmail(email).await()
        Log.i(TAG, "sendPasswordReset: email sent to $email")
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e(TAG, "sendPasswordReset: failed - ${e.message}")
        Result.failure(e)
    }

    suspend fun logout() {
        val uid = auth.currentUser?.uid
        auth.signOut()
        if (uid != null) logAccess(uid, "LOGOUT")
        Log.i(TAG, "logout: user signed out")
    }

    suspend fun getLocalUser(uid: String): UserEntity? = userDao.getUserById(uid)

    suspend fun saveLocalUser(user: UserEntity) {
        userDao.insert(user)
        Log.d(TAG, "saveLocalUser: ${user.username} saved")
    }

    suspend fun isUsernameTaken(username: String, excludeId: String = ""): Boolean =
        userDao.isUsernameTaken(username, excludeId)

    private suspend fun logAccess(userId: String, action: String) {
        accessLogDao.insert(AccessLogEntity(userId = userId, action = action, timestamp = System.currentTimeMillis()))
        Log.d(TAG, "logAccess: $action for $userId")
    }
}
