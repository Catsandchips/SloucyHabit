package com.slouchingdog.android.slouchyhabit.ui.habits_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.slouchingdog.android.slouchyhabit.data.Habit
import com.slouchingdog.android.slouchyhabit.data.HabitType
import com.slouchingdog.android.slouchyhabit.data.HabitsStorage

class HabitsListViewModel : ViewModel() {
    fun getHabits(habitTypeFilter: HabitType?): LiveData<List<Habit>> =
        HabitsStorage.habits.map { habits ->
            habits.filter { it.type == habitTypeFilter }
        }
}