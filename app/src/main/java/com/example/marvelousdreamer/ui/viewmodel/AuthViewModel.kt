package com.example.marvelousdreamer.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvelousdreamer.data.local.entity.UserEntity
import com.example.marvelousdreamer.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false,
    val successMessage: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    companion object { private const val TAG = "AuthViewModel" }

    private val _authState = MutableStateFlow(AuthState(isLoggedIn = authRepository.isLoggedIn))
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    val currentUserId: String? get() = authRepository.currentUser?.uid

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)
            val result = authRepository.login(email, password)
            result.fold(
                onSuccess = {
                    _authState.value = AuthState(isLoggedIn = true)
                    Log.i(TAG, "login: success")
                },
                onFailure = {
                    _authState.value = AuthState(error = it.message ?: "Login failed")
                    Log.e(TAG, "login: ${it.message}")
                }
            )
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)
            val result = authRepository.register(email, password)
            result.fold(
                onSuccess = {
                    _authState.value = AuthState(isLoggedIn = true, successMessage = "Verification email sent!")
                    Log.i(TAG, "register: success")
                },
                onFailure = {
                    _authState.value = AuthState(error = it.message ?: "Registration failed")
                    Log.e(TAG, "register: ${it.message}")
                }
            )
        }
    }

    fun sendPasswordReset(email: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)
            val result = authRepository.sendPasswordReset(email)
            result.fold(
                onSuccess = {
                    _authState.value = _authState.value.copy(isLoading = false, successMessage = "Recovery email sent!")
                    Log.i(TAG, "sendPasswordReset: sent to $email")
                },
                onFailure = {
                    _authState.value = _authState.value.copy(isLoading = false, error = it.message ?: "Failed to send email")
                    Log.e(TAG, "sendPasswordReset: ${it.message}")
                }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _authState.value = AuthState(isLoggedIn = false)
            Log.i(TAG, "logout: done")
        }
    }

    fun clearError() {
        _authState.value = _authState.value.copy(error = null, successMessage = null)
    }

    suspend fun getLocalUser(): UserEntity? {
        val uid = currentUserId ?: return null
        return authRepository.getLocalUser(uid)
    }

    fun saveLocalUser(user: UserEntity) {
        viewModelScope.launch {
            if (authRepository.isUsernameTaken(user.username, user.id)) {
                _authState.value = _authState.value.copy(error = "Username already in use")
                return@launch
            }
            authRepository.saveLocalUser(user)
        }
    }
}
