package com.slouchingdog.android.slouchyhabit.presentation.ui.create_habit.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.slouchingdog.android.slouchyhabit.R

@Composable
fun DescriptionFields(
    title: String,
    description: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit
) {
    Column {
        TextField(
            value = title,
            onValueChange = { value -> onTitleChange(value) },
            label = { Text(text = stringResource(R.string.habit_title_hint)) },
            colors = textFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = description,
            onValueChange = { value -> onDescriptionChange(value) },
            label = { Text(text = stringResource(R.string.habit_description_hint)) },
            colors = textFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}