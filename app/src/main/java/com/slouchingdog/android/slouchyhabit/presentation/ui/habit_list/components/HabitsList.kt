package com.slouchingdog.android.slouchyhabit.presentation.ui.habit_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slouchingdog.android.domain.entity.HabitEntity
import com.slouchingdog.android.slouchyhabit.R
import java.util.Locale

@Composable
fun HabitList(
    onNavigateToCreateHabit: (String?) -> Unit,
    habits: List<HabitEntity>,
    onDeleteButtonClick: (HabitEntity) -> Unit,
    onAddDoneDateButtonClick: (HabitEntity) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(habits) { habit ->
            HabitCard(
                onNavigateToCreateHabit = onNavigateToCreateHabit,
                habit = habit,
                onDeleteButtonClick = onDeleteButtonClick,
                onAddDoneDateButtonClick
            )
        }
    }
}

@Composable
fun HabitCard(
    onNavigateToCreateHabit: (String?) -> Unit,
    habit: HabitEntity,
    onDeleteButtonClick: (HabitEntity) -> Unit,
    onAddDoneDateButtonClick: (HabitEntity) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.highlight_color)),
        modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth(),
        onClick = {
            onNavigateToCreateHabit(habit.id)
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            HabitDescriptionColumn(habit)
            HabitModificationButtons(habit, onDeleteButtonClick, onAddDoneDateButtonClick)
        }
    }
}

@Composable
fun HabitDescriptionColumn(habit: HabitEntity) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = habit.title,
            color = colorResource(R.color.headers_color),
            fontSize = 34.sp
        )
        Text(
            text = habit.description,
            color = colorResource(R.color.headers_color),
            fontSize = 24.sp
        )
        HabitInfoRow(habit)
    }
}

@Composable
fun HabitInfoRow(habit: HabitEntity) {
    val context = LocalContext.current
    val timesCountString =
        context.resources.getQuantityString(R.plurals.times_in, habit.periodicityTimes)
    val daysCountString =
        context.resources.getQuantityString(R.plurals.days, habit.periodicityDays)

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = habit.type.title)
        Text(text = stringResource(R.string.devider_dot))
        Text(text = context.resources.getStringArray(R.array.priorities_array)[habit.priority])
        Text(text = stringResource(R.string.devider_dot))
        Text(
            text = String.format(
                Locale.getDefault(),
                "%d %s %d %s",
                habit.periodicityTimes,
                timesCountString,
                habit.periodicityDays,
                daysCountString
            )
        )
    }
}

@Composable
fun HabitModificationButtons(
    habit: HabitEntity,
    onDeleteButtonClick: (habit: HabitEntity) -> Unit,
    onAddDoneDateButtonClick: (habit: HabitEntity) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = { onDeleteButtonClick(habit) },
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.delete_button),
                tint = colorResource(R.color.white)
            )
        }

        IconButton(
            onClick = { onAddDoneDateButtonClick(habit) },
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = stringResource(R.string.habit_done),
                tint = colorResource(R.color.white)
            )
        }
    }
}