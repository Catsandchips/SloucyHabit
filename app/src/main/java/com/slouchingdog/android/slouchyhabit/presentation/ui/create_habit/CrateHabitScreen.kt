package com.slouchingdog.android.slouchyhabit.presentation.ui.create_habit

import androidx.activity.compose.LocalActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import com.slouchingdog.android.domain.entity.HabitType
import com.slouchingdog.android.slouchyhabit.R
import com.slouchingdog.android.slouchyhabit.presentation.compose_theme.SlouchyTheme
import com.slouchingdog.android.slouchyhabit.presentation.ui.create_habit.components.DescriptionFields
import com.slouchingdog.android.slouchyhabit.presentation.ui.create_habit.components.PeriodicityFields
import com.slouchingdog.android.slouchyhabit.presentation.ui.create_habit.components.PrioritySelector
import com.slouchingdog.android.slouchyhabit.presentation.ui.create_habit.components.TypeRadioGroup

@Composable
fun CreateHabitScreen(
    habitScreenStateLiveData: LiveData<HabitScreenState>,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPrioritySelection: (Int) -> Unit,
    onPrioritySelectorExpandedChange: () -> Unit,
    onDismissPriorityRequest: () -> Unit,
    onTypeSelected: (HabitType) -> Unit,
    onDaysChange: (String) -> Unit,
    onTimesChange: (String) -> Unit,
    onSaveButtonClick: () -> Unit
) {
    SlouchyTheme {
        val habitScreenState by habitScreenStateLiveData.observeAsState(HabitScreenState())
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            DescriptionFields(
                title = habitScreenState.title,
                description = habitScreenState.description,
                onTitleChange = onTitleChange,
                onDescriptionChange = onDescriptionChange
            )
            PrioritySelector(
                priority = habitScreenState.priority,
                isPrioritySelectorExpanded = habitScreenState.isPrioritySelectorExpanded,
                onPrioritySelection = onPrioritySelection,
                onPrioritySelectorExpandedChange = onPrioritySelectorExpandedChange,
                onDismissRequest = onDismissPriorityRequest
            )
            TypeRadioGroup(
                selectedType = habitScreenState.type,
                onTypeSelected = onTypeSelected
            )
            PeriodicityFields(
                times = habitScreenState.periodicityTimes,
                days = habitScreenState.periodicityDays,
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

