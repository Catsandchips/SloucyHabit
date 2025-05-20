package com.slouchingdog.android.slouchyhabit.presentation.compose_theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun SlouchyTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SlouchyDarkColorScheme,
        typography = SlouchyAppTypography,
        shapes = SlouchyShapes,
        content = content
    )
}