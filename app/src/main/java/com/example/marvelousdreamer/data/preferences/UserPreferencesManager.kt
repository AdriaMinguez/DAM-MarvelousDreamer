package com.example.marvelousdreamer.data.preferences

import android.content.Context
import android.util.Log

/**
 * Wraps SharedPreferences for persistent user settings (T1.4).
 * Required fields: username, dateOfBirth, darkMode, language.
 * Values are loaded automatically when the app starts.
 */
class UserPreferencesManager(context: Context) {

    companion object {
        private const val TAG   = "UserPrefsManager"
        private const val PREFS = "marvelous_dreamer_prefs"
    }

    private val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    // ── Stored fields (persisted via SharedPreferences) ───────────────────────

    var username: String
        get() = prefs.getString("username", "") ?: ""
        set(value) {
            prefs.edit().putString("username", value).apply()
            Log.d(TAG, "username saved: $value")
        }

    var dateOfBirth: String
        get() = prefs.getString("date_of_birth", "") ?: ""
        set(value) {
            prefs.edit().putString("date_of_birth", value).apply()
            Log.d(TAG, "dateOfBirth saved: $value")
        }

    var darkMode: Boolean
        get() = prefs.getBoolean("dark_mode", true)
        set(value) {
            prefs.edit().putBoolean("dark_mode", value).apply()
            Log.d(TAG, "darkMode saved: $value")
        }

    /** Language code: "en", "ca" or "es" */
    var language: String
        get() = prefs.getString("language", "en") ?: "en"
        set(value) {
            prefs.edit().putString("language", value).apply()
            Log.d(TAG, "language saved: $value")
        }

    var currency: String
        get() = prefs.getString("currency", "EUR (€)") ?: "EUR (€)"
        set(value) { prefs.edit().putString("currency", value).apply() }

    var travelReminders: Boolean
        get() = prefs.getBoolean("travel_reminders", true)
        set(value) { prefs.edit().putBoolean("travel_reminders", value).apply() }

    var weeklySummary: Boolean
        get() = prefs.getBoolean("weekly_summary", true)
        set(value) { prefs.edit().putBoolean("weekly_summary", value).apply() }

    var aiSuggestions: Boolean
        get() = prefs.getBoolean("ai_suggestions", false)
        set(value) { prefs.edit().putBoolean("ai_suggestions", value).apply() }
}
