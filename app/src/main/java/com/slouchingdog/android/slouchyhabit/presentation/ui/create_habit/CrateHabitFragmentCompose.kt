package com.slouchingdog.android.slouchyhabit.presentation.ui.create_habit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.slouchingdog.android.domain.entity.HabitType
import com.slouchingdog.android.slouchyhabit.R
import com.slouchingdog.android.slouchyhabit.presentation.compose_theme.SlouchyTheme

const val HABIT_ID_ARG = "HABIT_ARG"

@Composable
@Preview
fun CreateHabitScreen(habitId: String? = null) {
    SlouchyTheme {
        Column {
            TextField(
                value = "",
                onValueChange = {},
                label = { Text(text = stringResource(R.string.habit_title_hint)) })
            TextField(
                value = "",
                onValueChange = {},
                label = { Text(text = stringResource(R.string.habit_description_hint)) })
            Text(stringResource(R.string.priority_title))
            PrioritySelector()
            TypeRadioGroup()
            PeriodicityFields()
            Button(onClick = {}) { Text(stringResource(R.string.save_habit_button_text)) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioritySelector() {
    val context = LocalContext.current
    val options = context.resources.getStringArray(R.array.priorities_array)
    var expanded = remember { mutableStateOf(false) }
    var selectedOption = remember { mutableStateOf(options[0]) }

    ExposedDropdownMenuBox(
        expanded = false,
        onExpandedChange = { expanded.value = !expanded.value }) {
        TextField(
            modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryEditable),
            readOnly = true,
            value = selectedOption.value,
            onValueChange = {},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }) {
            options.forEach { selectedPriority ->
                DropdownMenuItem(
                    text = { Text(selectedPriority) },
                    onClick = {
                        selectedOption.value = selectedPriority
                        expanded.value = false
                    }
                )
            }
        }
    }
}

@Composable
fun TypeRadioGroup() {
    val types = listOf(HabitType.GOOD.title, HabitType.BAD.title)
    var selectedType = remember { mutableStateOf(types[0]) }

    Column {
        Text(stringResource(R.string.habit_type_header))
        types.forEach { type ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedType.value = type },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (type == selectedType.value),
                    onClick = { selectedType.value = type }
                )
                Text(
                    text = type
                )
            }
        }
    }
}

@Composable
fun PeriodicityFields() {
    var times = remember { mutableStateOf("") }
    var days = remember { mutableStateOf("") }
    Column {
        Text(text = stringResource(R.string.periodicity_title))
        Row {
            TextField(value = times.value, onValueChange = {})
            Text("")
            TextField(value = days.value, onValueChange = {})
            Text("")
        }
    }
}