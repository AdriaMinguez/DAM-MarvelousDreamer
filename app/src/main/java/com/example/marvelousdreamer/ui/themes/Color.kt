package com.example.marvelousdreamer.ui.themes

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Custom color system supporting dark and light themes.
 * All screens use AppTheme.colors.xxx instead of bare color constants.
 */
data class AppColors(
    // Backgrounds
    val bgDeep     : Color,
    val bgBase     : Color,
    val cardSurface: Color,
    val bgElevated : Color,
    val bgOutline  : Color,

    // Brand — violet
    val violet     : Color,
    val violetLight: Color,
    val violetSoft : Color,

    // Accent — green
    val emerald     : Color,
    val emeraldLight: Color,
    val emeraldSoft : Color,

    // Gradients
    val gradStart: Color,
    val gradMid  : Color,
    val gradEnd  : Color,

    // Semantic
    val amber: Color,
    val rose : Color,

    // Text
    val snow: Color,
    val mist: Color,
    val fog : Color
)

// ── Dark palette ──────────────────────────────────────────────────────────────

val DarkAppColors = AppColors(
    bgDeep      = Color(0xFF0A0710),
    bgBase      = Color(0xFF100D1A),
    cardSurface = Color(0xFF1A1528),
    bgElevated  = Color(0xFF231D35),
    bgOutline   = Color(0xFF2E2647),

    violet      = Color(0xFF7C3AED),
    violetLight = Color(0xFF9F67F5),
    violetSoft  = Color(0xFFB794F6),

    emerald      = Color(0xFF10B981),
    emeraldLight = Color(0xFF34D399),
    emeraldSoft  = Color(0xFF6EE7B7),

    gradStart = Color(0xFF6D28D9),
    gradMid   = Color(0xFF4F46E5),
    gradEnd   = Color(0xFF059669),

    amber = Color(0xFFF59E0B),
    rose  = Color(0xFFEF4444),

    snow = Color(0xFFFFFFFF),
    mist = Color(0xFFD4BBFF),
    fog  = Color(0xFF6B5F8A)
)

// ── Light palette ─────────────────────────────────────────────────────────────

val LightAppColors = AppColors(
    bgDeep      = Color(0xFFEDE8F5),
    bgBase      = Color(0xFFF7F5FC),
    cardSurface = Color(0xFFFFFFFF),
    bgElevated  = Color(0xFFFFFFFF),
    bgOutline   = Color(0xFFDDD7EB),

    violet      = Color(0xFF7C3AED),
    violetLight = Color(0xFF6D28D9),
    violetSoft  = Color(0xFF8B5CF6),

    emerald      = Color(0xFF059669),
    emeraldLight = Color(0xFF10B981),
    emeraldSoft  = Color(0xFF34D399),

    gradStart = Color(0xFF7C3AED),
    gradMid   = Color(0xFF6366F1),
    gradEnd   = Color(0xFF10B981),

    amber = Color(0xFFD97706),
    rose  = Color(0xFFDC2626),

    snow = Color(0xFF1A1528),
    mist = Color(0xFF4A3F6B),
    fog  = Color(0xFF8E85A8)
)

// ── CompositionLocal ──────────────────────────────────────────────────────────

val LocalAppColors = compositionLocalOf { DarkAppColors }

// ── Legacy aliases (kept so old code can gradually migrate) ────────────────────
// These are ONLY used as fallback defaults. Screens should use AppTheme.colors.
val BgDeep      = DarkAppColors.bgDeep
val BgBase      = DarkAppColors.bgBase
val CardSurface = DarkAppColors.cardSurface
val BgElevated  = DarkAppColors.bgElevated
val BgOutline   = DarkAppColors.bgOutline
val Violet      = DarkAppColors.violet
val VioletLight = DarkAppColors.violetLight
val VioletSoft  = DarkAppColors.violetSoft
val Emerald     = DarkAppColors.emerald
val EmeraldLight= DarkAppColors.emeraldLight
val EmeraldSoft = DarkAppColors.emeraldSoft
val GradStart   = DarkAppColors.gradStart
val GradMid     = DarkAppColors.gradMid
val GradEnd     = DarkAppColors.gradEnd
val Amber       = DarkAppColors.amber
val Rose        = DarkAppColors.rose
val Snow        = DarkAppColors.snow
val Mist        = DarkAppColors.mist
val Fog         = DarkAppColors.fog
