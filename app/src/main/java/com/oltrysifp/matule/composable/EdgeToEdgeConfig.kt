package com.oltrysifp.matule.composable

import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color

@Composable
fun EdgeToEdgeConfig(
    activity: ComponentActivity,
    topBarColor: Color = Color.Transparent,
    bottomBarColor: Color = Color.Transparent
) {
    val statusBar = topBarColor.hashCode()
    val bottomBar = bottomBarColor.hashCode()

    activity.enableEdgeToEdge(
        statusBarStyle = SystemBarStyle.auto(statusBar, statusBar),
        navigationBarStyle = SystemBarStyle.auto(bottomBar, statusBar)
    )
}