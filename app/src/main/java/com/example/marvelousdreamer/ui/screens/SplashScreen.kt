package com.example.marvelousdreamer.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marvelousdreamer.R
import com.example.marvelousdreamer.ui.themes.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {

    val appName     = stringResource(R.string.app_name)
    val appTagline  = stringResource(R.string.app_tagline)
    val appVersion  = stringResource(R.string.app_version)
    val appSprint   = stringResource(R.string.app_sprint)
    val loadingText = stringResource(R.string.splash_loading)

    var contentAlpha by remember { mutableFloatStateOf(0f) }
    var logoScale    by remember { mutableFloatStateOf(0.7f) }
    var progress     by remember { mutableFloatStateOf(0f) }

    val animAlpha    = animateFloatAsState(targetValue = contentAlpha,
        animationSpec = tween(900, easing = EaseOut), label = "alpha")
    val animScale    = animateFloatAsState(targetValue = logoScale,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow), label = "scale")
    val animProgress = animateFloatAsState(targetValue = progress,
        animationSpec = tween(2200, easing = EaseInOut), label = "progress")

    LaunchedEffect(Unit) {
        contentAlpha = 1f; logoScale = 1f
        delay(400); progress = 1f
        delay(2500); onFinished()
    }

    Box(
        modifier         = Modifier.fillMaxSize().background(BgBase),
        contentAlignment = Alignment.Center
    ) {
        // Glow morat
        Box(
            modifier = Modifier.size(360.dp).background(
                Brush.radialGradient(listOf(Violet.copy(alpha = 0.20f), BgBase.copy(alpha = 0f)))
            )
        )
        // Glow verd inferior
        Box(
            modifier = Modifier.size(280.dp).offset(y = 120.dp).background(
                Brush.radialGradient(listOf(Emerald.copy(alpha = 0.10f), BgBase.copy(alpha = 0f)))
            )
        )

        Column(
            modifier            = Modifier.fillMaxWidth().alpha(animAlpha.value).padding(horizontal = 52.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            androidx.compose.foundation.Image(
                painter            = painterResource(R.drawable.marvelousdreamer_circle_),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(180.dp)
                    .scale(animScale.value)
                    .clip(RoundedCornerShape(48.dp))
            )

            Spacer(Modifier.height(28.dp))

            Text(
                text       = appName,
                style      = MaterialTheme.typography.displayLarge,
                color      = Snow,
                fontWeight = FontWeight.ExtraBold,
                textAlign  = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text      = appTagline,
                style     = MaterialTheme.typography.bodyMedium,
                color     = Mist,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(64.dp))

            // Barra de progrés amb gradient morat→verd
            Box(
                modifier = Modifier.fillMaxWidth().height(3.dp).clip(RoundedCornerShape(2.dp))
                    .background(BgOutline)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animProgress.value)
                        .fillMaxHeight()
                        .background(Brush.horizontalGradient(listOf(Violet, Emerald)))
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text          = loadingText,
                style         = MaterialTheme.typography.labelSmall,
                color         = Fog,
                letterSpacing = 2.sp
            )

            Spacer(Modifier.height(52.dp))

            Text(
                text  = "v$appVersion · $appSprint",
                style = MaterialTheme.typography.bodySmall,
                color = Fog.copy(alpha = 0.6f)
            )
        }
    }
}