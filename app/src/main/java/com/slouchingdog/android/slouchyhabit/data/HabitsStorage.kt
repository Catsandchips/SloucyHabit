package com.slouchingdog.android.slouchyhabit.data

class HabitsStorage {
    companion object {
        val habits = mutableListOf<Habit>()

        fun getHabitsWithType(habitType: HabitType?) = habits.filter { it.type == habitType }
    }
}