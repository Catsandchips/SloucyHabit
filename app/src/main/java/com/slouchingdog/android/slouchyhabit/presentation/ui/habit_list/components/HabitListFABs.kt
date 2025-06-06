package com.slouchingdog.android.slouchyhabit.presentation.ui.habit_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.slouchingdog.android.slouchyhabit.R

@Composable
fun HabitListFABs(onOpenFilterFABClick: () -> Unit, onNavigateToCreateHabit: (String?) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        FloatingActionButton(onClick = onOpenFilterFABClick) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.open_filter_button_description)
            )
        }
        FloatingActionButton(
            onClick = { onNavigateToCreateHabit(null) },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.testTag("createHabitButton")
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add_habit_button_description)
            )
        }
    }
}