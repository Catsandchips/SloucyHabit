package com.slouchingdog.android.slouchyhabit.presentation.compose_theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

@Composable
fun BaseSlouchyTheme(
    content: @Composable () -> Unit,
    typography: Typography = SlouchyAppTypography
) {
    MaterialTheme(
        colorScheme = SlouchyDarkColorScheme,
        typography = typography,
        shapes = SlouchyShapes,
        content = content
    )
}

@Composable
fun CreateHabitTheme(
    content: @Composable () -> Unit
){
    BaseSlouchyTheme(content, CreateHabitTypography)
}

