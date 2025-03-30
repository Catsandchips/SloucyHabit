package com.slouchingdog.android.slouchyhabit.ui.create_habit

import androidx.lifecycle.ViewModel
import com.slouchingdog.android.slouchyhabit.data.Habit
import com.slouchingdog.android.slouchyhabit.data.HabitType
import com.slouchingdog.android.slouchyhabit.data.HabitsRepository

class CreateHabitViewModel : ViewModel() {
    private val habitsRepository = HabitsRepository.get()
    fun addHabit(
        id: Int?,
        title: String,
        description: String,
        type: HabitType,
        priority: Int,
        periodicityTimes: Int,
        periodicityDays: Int
    ) {
        val habit = Habit(
            id = id,
            title = title,
            description = description,
            type = type,
            priority = priority,
            periodicityTimes = periodicityTimes,
            periodicityDays = periodicityDays,
        )

        habitsRepository.addHabit(habit)
    }
}