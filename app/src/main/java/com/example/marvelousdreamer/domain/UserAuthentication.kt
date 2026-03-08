package com.example.marvelousdreamer.domain

/**
 * Manages login state and authentication provider for a User.
 * Currently supports email/password. Google OAuth is a future feature.
 * Renamed from Authentication to avoid collision with com.google.api.Authentication.
 */
data class UserAuthentication(
    val userId     : String,
    val isLoggedIn : Boolean = false,
    val lastLoginAt: Long?   = null,
    val provider   : String  = "email"
) {
    /**
     * Future feature: authenticate with Firebase or custom backend.
     */
    fun login(email: String, password: String): UserAuthentication {
        // @TODO Implement login with backend/Firebase
        return this
    }

    /**
     * Future feature: clear session token and return logged-out copy.
     */
    fun logout(): UserAuthentication {
        // @TODO Clear session token
        return copy(isLoggedIn = false)
    }

    /**
     * Future feature: send a password reset email.
     */
    fun resetPassword(email: String) {
        // @TODO Trigger Firebase password reset email
    }

    /**
     * Future feature: Google Sign-In OAuth flow.
     */
    fun loginWithGoogle(): UserAuthentication {
        // @TODO Implement Google Sign-In
        return this
    }

    /**
     * Future feature: validate old password and set new one.
     */
    fun updatePassword(oldPassword: String, newPassword: String) {
        // @TODO Validate old password before updating
    }
}
