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
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        val prefsManager = UserPreferencesManager(this)
        applyLocale(prefsManager.language)

        setContent {
            var isDarkMode by remember { mutableStateOf(prefsManager.darkMode) }

            MarvelousDreamerTheme(darkTheme = isDarkMode) {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    onDarkModeChanged = { isDarkMode = it },
                    onLanguageChanged = { lang ->
                        applyLocale(lang)
                        recreate()
                    }
                )
            }
        }
    }

    private fun applyLocale(langCode: String) {
        val locale = Locale(langCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        @Suppress("DEPRECATION")
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}
