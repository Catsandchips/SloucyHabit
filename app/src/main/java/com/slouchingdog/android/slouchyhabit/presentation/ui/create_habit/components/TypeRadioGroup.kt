package com.slouchingdog.android.slouchyhabit.presentation.ui.create_habit.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.slouchingdog.android.domain.entity.HabitType
import com.slouchingdog.android.slouchyhabit.R

@Composable
fun TypeRadioGroup(selectedType: HabitType, onTypeSelected: (HabitType) -> Unit) {
    val types = listOf(HabitType.GOOD.title, HabitType.BAD.title)

    Column {
        Text(
            text = stringResource(R.string.habit_type_header),
            fontSize = 34.sp,
            color = MaterialTheme.colorScheme.primary
        )
        types.forEach { type ->
            val habitType = HabitType.entries.find { it.title == type } ?: HabitType.GOOD

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onTypeSelected(habitType) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (type == selectedType.title),
                    onClick = { onTypeSelected(habitType) }
                )
                Text(
                    text = type
                )
            }
        }
    }
}