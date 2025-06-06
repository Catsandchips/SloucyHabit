package com.slouchingdog.android.slouchyhabit.presentation.ui.create_habit.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slouchingdog.android.slouchyhabit.R


@Composable
fun textFieldColors() = TextFieldDefaults.colors(
    unfocusedContainerColor = Color.Transparent,
    focusedContainerColor = Color.Transparent
)

@Composable
fun PeriodicityFields(
    times: String,
    days: String,
    onTimesChange: (String) -> Unit,
    onDaysChange: (String) -> Unit
) {
    val context = LocalContext.current
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(R.string.periodicity_title),
            fontSize = 34.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            TextField(
                modifier = Modifier.width(40.dp),
                label = { Text("") },
                value = times.toString(),
                onValueChange = { value -> onTimesChange(value) },
                colors = textFieldColors()
            )
            Text(
                text = context.resources.getQuantityString(
                    R.plurals.times_in,
                    times.toIntOrNull() ?: 0
                )
            )
            TextField(
                modifier = Modifier.width(40.dp),
                label = { Text("") },
                value = days.toString(),
                onValueChange = { value -> onDaysChange(value) },
                colors = textFieldColors()
            )
            Text(
                text = context.resources.getQuantityString(
                    R.plurals.days,
                    days.toIntOrNull() ?: 0
                )
            )
        }
    }
}