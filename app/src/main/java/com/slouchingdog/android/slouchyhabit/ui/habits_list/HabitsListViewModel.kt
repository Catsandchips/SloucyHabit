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
    var sortingData: SortingData = SortingData(false, false)
    var titleQuery: String? = null

    init {
        HabitsStorage.habits.observeForever { habits ->
            filteredHabits.value = habits ?: emptyList()
            titleQuery = null
        }
    }

    fun getHabits(habitTypeFilter: HabitType?): LiveData<List<Habit>> = filteredHabits.map { habits ->
            habits.filter { it.type == habitTypeFilter }
        }

    fun setFilters(titleQuery: String?) {
        this.titleQuery = titleQuery
        var habits = HabitsStorage.habits.value ?: emptyList()

        var filteredList = habits.filter { habit ->
            (titleQuery.isNullOrEmpty() || habit.title.contains(titleQuery, true))
        }

        if (sortingData.needSorting){
            filteredList = sortHabits(filteredList)
        }

        filteredHabits.value = filteredList
    }

    fun setPrioritySorting(sortAsc: Boolean) {
        sortingData = SortingData(true, sortAsc)
        setFilters(titleQuery)
    }

    fun resetPrioritySorting() {
        sortingData = SortingData(false, false)
        setFilters(titleQuery)
    }

    private fun sortHabits(habits: List<Habit>) =
        if (sortingData.sortAsc) habits.sortedBy { it.priority } else habits.sortedByDescending { it.priority }


}

data class SortingData(val needSorting: Boolean, val sortAsc: Boolean)
