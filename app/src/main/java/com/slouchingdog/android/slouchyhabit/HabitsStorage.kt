package com.slouchingdog.android.slouchyhabit

class HabitsStorage {
    companion object {
        val habits = mutableListOf(Habit(0, "Fuck", "i dont", HabitType.GOOD, "Высокий приоритет", 1, 1), Habit(0, "NICE", "i dont nienv", HabitType.BAD, "Высокий приоритет", 1, 1))

        fun getHabitsWithType(habitType: HabitType?) = habits.filter { it.type == habitType }
    }
}