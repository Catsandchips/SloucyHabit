package com.slouchingdog.android.slouchyhabit.data

class HabitsStorage {
    companion object {
        val habits = mutableListOf<Habit>()

        fun getHabitsWithType(habitType: HabitType?) = habits.filter { it.type == habitType }

        fun addHabit(habit: Habit) {
            val index = habits.indexOfFirst { it.id == habit.id }
            if (index != -1) {
                habits[index] = habit
            } else {
                habits.add(habit)
            }
        }
    }
}