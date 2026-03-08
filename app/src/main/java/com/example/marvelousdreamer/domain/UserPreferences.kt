package com.example.marvelousdreamer.domain

/**
 * Stores all user-configurable settings.
 * Renamed from Preferences to avoid collision with java.util.prefs.Preferences.
 */
data class UserPreferences(
    val language     : String               = "en",
    val currency     : String               = "EUR",
    val dateFormat   : String               = "DD/MM/YYYY",
    val darkMode     : Boolean              = true,
    val textSize     : String               = "Normal",
    val notifications: NotificationSettings = NotificationSettings()
) {
    /**
     * Returns a new UserPreferences with the given language applied.
     */
    fun updateLanguage(lang: String): UserPreferences {
        return copy(language = lang)
    }

    /**
     * Returns a new UserPreferences with dark mode toggled.
     */
    fun toggleDarkMode(): UserPreferences {
        return copy(darkMode = !darkMode)
    }

    /**
     * Future feature: sync preferences to backend API.
     */
    fun syncToCloud() {
        // @TODO Sync preferences to backend
    }

    /**
     * Future feature: reset all fields to their default values.
     */
    fun resetToDefaults(): UserPreferences {
        // @TODO Also call saveLanguage() and saveTheme() in PreferencesScreen
        return UserPreferences()
    }
}
