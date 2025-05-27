package com.slouchingdog.android.slouchyhabit.presentation.ui.create_habit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import com.slouchingdog.android.domain.entity.HabitType
import com.slouchingdog.android.slouchyhabit.R
import com.slouchingdog.android.slouchyhabit.presentation.compose_theme.SlouchyTheme

@Composable
fun textFieldColors() = TextFieldDefaults.colors(
    unfocusedContainerColor = Color.Transparent,
    focusedContainerColor = Color.Transparent
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun exposedTextFieldColors() = ExposedDropdownMenuDefaults.textFieldColors(
    unfocusedContainerColor = Color.Transparent,
    focusedContainerColor = Color.Transparent
)

@Composable
fun CreateHabitScreen(
    habitId: String? = null,
    habitStateLiveData: LiveData<HabitState>,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPrioritySelection: (Int) -> Unit,
    onTypeSelected: (HabitType) -> Unit,
    onDaysChange: (String) -> Unit,
    onTimesChange: (String) -> Unit,
    onSaveButtonClick: () -> Unit
) {
    SlouchyTheme {
        val habitState by habitStateLiveData.observeAsState(HabitState())
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            DescriptionFields(
                title = habitState.title,
                description = habitState.description,
                onTitleChange = onTitleChange,
                onDescriptionChange = onDescriptionChange
            )
            PrioritySelector(
                priority = habitState.priority,
                onPrioritySelection = onPrioritySelection
            )
            TypeRadioGroup(
                selectedType = habitState.type,
                onTypeSelected = onTypeSelected
            )
            PeriodicityFields(
                times = habitState.periodicityTimes,
                days = habitState.periodicityDays,
                onDaysChange = onDaysChange,
                onTimesChange = onTimesChange
            )
            Button(onClick = { onSaveButtonClick() }) {
                Text(
                    text = stringResource(R.string.save_habit_button_text),
                    fontSize = 34.sp,
                    color = MaterialTheme.colorScheme.background
                )
            }
        }
    }
}

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioritySelector(priority: Int, onPrioritySelection: (Int) -> Unit) {
    val context = LocalContext.current
    val options = context.resources.getStringArray(R.array.priorities_array)
    val selectedPriority = options[priority]
    var expanded = remember { mutableStateOf(false) }

    Column {
        Text(
            text = stringResource(R.string.priority_title),
            fontSize = 34.sp,
            color = MaterialTheme.colorScheme.primary
        )
        ExposedDropdownMenuBox(
            expanded = false,
            onExpandedChange = { expanded.value = !expanded.value }) {
            TextField(
                modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryEditable),
                readOnly = true,
                value = selectedPriority,
                onValueChange = {},
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
                colors = exposedTextFieldColors()
            )

            ExposedDropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }) {
                options.forEach { selectedPriority ->
                    DropdownMenuItem(
                        text = { Text(selectedPriority) },
                        onClick = {
                            onPrioritySelection(options.indexOf(selectedPriority))
                            expanded.value = false
                        }
                    )
                }
            }
        }
    }
}

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
                modifier = Modifier.width(64.dp),
                label = {
                    Text(
                        text = context.resources.getQuantityString(
                            R.plurals.times,
                            times.toIntOrNull() ?: 0
                        )
                    )
                },
                value = times.toString(),
                onValueChange = { value -> onTimesChange(value) },
                colors = textFieldColors()
            )
            Text(text = "в")
            TextField(
                modifier = Modifier.width(64.dp),
                label = {
                    Text(
                        text = context.resources.getQuantityString(
                            R.plurals.days,
                            days.toIntOrNull() ?: 0
                        )
                    )
                },
                value = days.toString(),
                onValueChange = { value -> onDaysChange(value) },
                colors = textFieldColors()
            )
        }
    }
}