package com.slouchingdog.android.slouchyhabit.ui.create_habit

import androidx.lifecycle.ViewModel
import com.slouchingdog.android.slouchyhabit.data.Habit
import com.slouchingdog.android.slouchyhabit.data.HabitType
import com.slouchingdog.android.slouchyhabit.data.HabitsStorage
import java.util.UUID

class CreateHabitViewModel : ViewModel() {
    fun addHabit(
        id: UUID?,
        title: String,
        description: String,
        type: HabitType,
        priority: Int,
        periodicityTimes: Int,
        periodicityDays: Int
    ) {
        val habit = Habit(
            id ?: UUID.randomUUID(),
            title,
            description,
            type,
            priority,
            periodicityTimes,
            periodicityDays
        )

        HabitsStorage.addHabit(habit)
    }
}