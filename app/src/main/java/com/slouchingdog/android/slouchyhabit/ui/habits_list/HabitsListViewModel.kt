package com.slouchingdog.android.slouchyhabit.ui.habits_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.slouchingdog.android.slouchyhabit.data.Habit
import com.slouchingdog.android.slouchyhabit.data.HabitType
import com.slouchingdog.android.slouchyhabit.data.HabitsStorage

class HabitsListViewModel() : ViewModel() {
    val filteredHabits = MutableLiveData<List<Habit>>()
    var titleQuery: String? = null


    init {
        HabitsStorage.habits.observeForever { habits ->
            filteredHabits.value = habits ?: emptyList()
            titleQuery = null
        }
    }

    fun getHabits(habitTypeFilter: HabitType?): LiveData<List<Habit>> =
        filteredHabits.map { habits ->
            habits.filter { it.type == habitTypeFilter }
        }

    fun setFilters(titleQuery: String?) {
        this.titleQuery = titleQuery
        var habits = HabitsStorage.habits.value ?: emptyList()

        val filteredList = habits.filter { habit ->
            (titleQuery.isNullOrEmpty() || habit.title.contains(titleQuery, true))
        }

        filteredHabits.value = filteredList
    }

    fun setPrioritySorting(sortAsc: Boolean) {
        val habits = filteredHabits.value ?: emptyList()

        val sortedList =
            if (sortAsc) habits.sortedBy { it.priority } else habits.sortedByDescending { it.priority }

        filteredHabits.value = sortedList
    }
}