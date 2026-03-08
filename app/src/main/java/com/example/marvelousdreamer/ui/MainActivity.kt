package com.example.marvelousdreamer.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.marvelousdreamer.ui.navigation.NavGraph
import com.example.marvelousdreamer.ui.themes.MarvelousDreamerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            MarvelousDreamerTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}