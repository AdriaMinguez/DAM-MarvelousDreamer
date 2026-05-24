package com.example.marvelousdreamer.data.repository

import android.util.Log
import com.example.marvelousdreamer.data.local.dao.AccessLogDao
import com.example.marvelousdreamer.data.local.dao.UserDao
import com.example.marvelousdreamer.data.local.entity.AccessLogEntity
import com.example.marvelousdreamer.data.local.entity.UserEntity
import com.example.marvelousdreamer.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val userDao: UserDao,
    private val accessLogDao: AccessLogDao
) : AuthRepository {

    companion object { private const val TAG = "AuthRepository" }

    override val currentUser: FirebaseUser? get() = auth.currentUser

    override val isLoggedIn: Boolean get() = auth.currentUser != null

    override suspend fun login(email: String, password: String): Result<FirebaseUser> = try {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val user = result.user!!
        logAccess(user.uid, "LOGIN")
        Log.i(TAG, "login: success for ${user.email}")
        Result.success(user)
    } catch (e: Exception) {
        Log.e(TAG, "login: failed - ${e.message}")
        Result.failure(e)
    }

    override suspend fun register(email: String, password: String): Result<FirebaseUser> = try {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user!!
        user.sendEmailVerification().await()
        userDao.insert(UserEntity(id = user.uid, login = email, username = email.substringBefore("@")))
        logAccess(user.uid, "LOGIN")
        Log.i(TAG, "register: success for ${user.email}, verification email sent")
        Result.success(user)
    } catch (e: Exception) {
        Log.e(TAG, "register: failed - ${e.message}")
        Result.failure(e)
    }

    override suspend fun sendPasswordReset(email: String): Result<Unit> = try {
        auth.sendPasswordResetEmail(email).await()
        Log.i(TAG, "sendPasswordReset: email sent to $email")
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e(TAG, "sendPasswordReset: failed - ${e.message}")
        Result.failure(e)
    }

    override suspend fun logout() {
        val uid = auth.currentUser?.uid
        auth.signOut()
        if (uid != null) logAccess(uid, "LOGOUT")
        Log.i(TAG, "logout: user signed out")
    }

    override suspend fun getLocalUser(uid: String): UserEntity? = userDao.getUserById(uid)

    override suspend fun saveLocalUser(user: UserEntity) {
        userDao.insert(user)
        Log.d(TAG, "saveLocalUser: ${user.username} saved")
    }

    override suspend fun isUsernameTaken(username: String, excludeId: String): Boolean =
        userDao.isUsernameTaken(username, excludeId)

    private suspend fun logAccess(userId: String, action: String) {
        accessLogDao.insert(AccessLogEntity(userId = userId, action = action, timestamp = System.currentTimeMillis()))
        Log.d(TAG, "logAccess: $action for $userId")
    }
}