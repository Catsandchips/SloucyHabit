package com.slouchingdog.android.slouchyhabit.ui.habits_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.slouchingdog.android.slouchyhabit.data.Habit
import com.slouchingdog.android.slouchyhabit.data.HabitType
import com.slouchingdog.android.slouchyhabit.data.HabitsStorage

class HabitsListViewModel() : ViewModel() {
    private val filteredHabits = MutableLiveData<List<Habit>>()
    var sortingData: SortingData = SortingData(false, false)
    var titleQuery: String? = null

    init {
        HabitsStorage.habits.observeForever { habits ->
            filteredHabits.value = habits.orEmpty()
            titleQuery = null
            sortingData.needSorting = false
        }
    }

    fun getHabits(habitTypeFilter: HabitType?): LiveData<List<Habit>> =
        filteredHabits.map { habits ->
            habits.filter { it.type == habitTypeFilter }
        }

    fun filterHabits(titleQuery: String?) {
        this.titleQuery = titleQuery
        var habits = HabitsStorage.habits.value.orEmpty()

        var filteredList = habits.filter { habit ->
            (titleQuery.isNullOrEmpty() || habit.title.contains(titleQuery, true))
        }

        if (sortingData.needSorting) {
            filteredList = sortHabitsByPriority(filteredList)
        }

        filteredHabits.value = filteredList
    }

    fun setPrioritySorting(sortByAsc: Boolean) {
        sortingData.apply {
            needSorting = true
            sortAsc = sortByAsc
        }
        filterHabits(titleQuery)
    }

    fun resetPrioritySorting() {
        sortingData.needSorting = false
        filterHabits(titleQuery)
    }

    private fun sortHabitsByPriority(habits: List<Habit>) =
        if (sortingData.sortAsc) habits.sortedBy { it.priority } else habits.sortedByDescending { it.priority }
}

data class SortingData(var needSorting: Boolean, var sortAsc: Boolean)
