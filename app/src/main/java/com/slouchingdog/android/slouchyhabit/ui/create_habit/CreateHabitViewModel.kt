package com.slouchingdog.android.slouchyhabit.ui.create_habit

import androidx.lifecycle.ViewModel
import com.slouchingdog.android.slouchyhabit.data.Habit
import com.slouchingdog.android.slouchyhabit.data.HabitType
import com.slouchingdog.android.slouchyhabit.data.HabitsRepository

class CreateHabitViewModel() : ViewModel() {
    private val habitsRepository = HabitsRepository.get()
    var habitState: HabitState = HabitState()

    fun setHabitState(habitId: Int) {
        val habit = habitsRepository.getHabitById(habitId)
        habitState = HabitState(
            habit.id,
            habit.title,
            habit.description,
            habit.type,
            habit.priority,
            habit.periodicityTimes,
            habit.periodicityDays
        )
    }

    fun addHabit() {
        val habit = Habit(
            id = habitState.id,
            title = habitState.title,
            description = habitState.description,
            type = habitState.type,
            priority = habitState.priority,
            periodicityTimes = habitState.periodicityTimes,
            periodicityDays = habitState.periodicityDays
        )

        habitsRepository.addHabit(habit)
    }

    fun onTitleChange(newTitle: String) {
        habitState = habitState.copy(title = newTitle)
    }

    fun onDescriptionChange(newDescription: String) {
        habitState = habitState.copy(description = newDescription)
    }

    fun onTypeChange(newType: HabitType) {
        habitState = habitState.copy(type = newType)
    }

    fun onPriorityChange(newPriority: Int) {
        habitState = habitState.copy(priority = newPriority)
    }

    fun onPeriodicityTimesChange(newPeriodicityTimes: Int) {
        habitState = habitState.copy(periodicityTimes = newPeriodicityTimes)
    }

    fun onPeriodicityDaysChange(newPeriodicityDays: Int) {
        habitState = habitState.copy(periodicityDays = newPeriodicityDays)
    }
}

data class HabitState(
    val id: Int? = null,
    val title: String = "",
    val description: String = "",
    val type: HabitType = HabitType.GOOD,
    val priority: Int = 0,
    val periodicityTimes: Int = 0,
    val periodicityDays: Int = 0
)