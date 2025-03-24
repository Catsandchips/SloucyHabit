package com.slouchingdog.android.slouchyhabit.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class HabitsStorage {
    companion object {
        private val _habits = MutableLiveData<List<Habit>>(mutableListOf())
        val habits: LiveData<List<Habit>> = _habits

        fun addHabit(habit: Habit) {
            val currentList = _habits.value.orEmpty().toMutableList()
            val index = currentList.indexOfFirst { it.id == habit.id }
            if (index != -1) {
                currentList[index] = habit
            } else {
                currentList.add(habit)
            }
            _habits.value = currentList
        }
    }
}