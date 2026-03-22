package com.example.marvelousdreamer.ui.themes

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary          = DarkAppColors.violet,
    onPrimary        = DarkAppColors.snow,
    secondary        = DarkAppColors.emerald,
    onSecondary      = DarkAppColors.snow,
    background       = DarkAppColors.bgBase,
    onBackground     = DarkAppColors.snow,
    surface          = DarkAppColors.cardSurface,
    onSurface        = DarkAppColors.snow,
    surfaceVariant   = DarkAppColors.bgElevated,
    onSurfaceVariant = DarkAppColors.mist,
    outline          = DarkAppColors.bgOutline,
    error            = DarkAppColors.rose,
    onError          = DarkAppColors.snow,
)

private val LightColorScheme = lightColorScheme(
    primary          = LightAppColors.violet,
    onPrimary        = LightAppColors.bgBase,
    secondary        = LightAppColors.emerald,
    onSecondary      = LightAppColors.bgBase,
    background       = LightAppColors.bgBase,
    onBackground     = LightAppColors.snow,
    surface          = LightAppColors.cardSurface,
    onSurface        = LightAppColors.snow,
    surfaceVariant   = LightAppColors.bgElevated,
    onSurfaceVariant = LightAppColors.mist,
    outline          = LightAppColors.bgOutline,
    error            = LightAppColors.rose,
    onError          = LightAppColors.bgBase,
)

@Composable
fun MarvelousDreamerTheme(
    darkTheme    : Boolean = isSystemInDarkTheme(),
    dynamicColor : Boolean = false,
    content      : @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else      -> LightColorScheme
    }

    val appColors = if (darkTheme) DarkAppColors else LightAppColors

    CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography  = Typography,
            content     = content
        )
    }
}

/**
 * Convenience accessor for custom app colors.
 * Usage: AppTheme.colors.bgBase, AppTheme.colors.violet, etc.
 */
object AppTheme {
    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current
}
