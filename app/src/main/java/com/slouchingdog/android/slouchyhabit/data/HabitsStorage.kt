package com.slouchingdog.android.slouchyhabit.data

class HabitsStorage {
    companion object {
        private val _habits = mutableListOf<Habit>()
        val habits = _habits

        fun addHabit(habit: Habit) {
            val index = _habits.indexOfFirst { it.id == habit.id }
            if (index != -1) {
                _habits[index] = habit
            } else {
                _habits.add(habit)
            }
        }

        fun getHabitsWithType(habitType: HabitType?) = habits.filter { it.type == habitType }
    }
}