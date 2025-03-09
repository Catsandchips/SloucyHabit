package com.slouchingdog.android.slouchyhabit

class HabitsStorage {
    companion object {
        val habits = mutableListOf<Habit>()

        fun getHabitsWithType(habitType: HabitType) = habits.filter { it.type == habitType }
    }
}