package com.example.marvelousdreamer.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.marvelousdreamer.data.preferences.UserPreferencesManager
import com.example.marvelousdreamer.ui.navigation.NavGraph
import com.example.marvelousdreamer.ui.themes.MarvelousDreamerTheme
import java.util.Locale

/**
 * Main entry point — Sprint 02.
 * Loads saved preferences (dark mode + language) on startup (T1.4).
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        // Load saved language preference and apply locale (T1.5)
        val prefsManager = UserPreferencesManager(this)
        applyLocale(prefsManager.language)

        setContent {
            // Read dark mode preference (T1.4)
            var isDarkMode by remember { mutableStateOf(prefsManager.darkMode) }

            MarvelousDreamerTheme(darkTheme = isDarkMode) {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    onDarkModeChanged = { isDarkMode = it },
                    onLanguageChanged = { lang ->
                        applyLocale(lang)
                        recreate() // Restart activity to apply new locale
                    }
                )
            }
        }
    }

    /**
     * Applies the given language code ("en", "ca", "es") to the app's locale.
     * This ensures the correct values-xx/strings.xml is used.
     */
    private fun applyLocale(langCode: String) {
        val locale = Locale(langCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        @Suppress("DEPRECATION")
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}
