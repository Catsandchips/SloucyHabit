package com.slouchingdog.android.slouchyhabit.ui.habits_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slouchingdog.android.slouchyhabit.data.Habit
import com.slouchingdog.android.slouchyhabit.data.HabitType
import com.slouchingdog.android.slouchyhabit.data.HabitsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HabitsListViewModel() : ViewModel() {
    private val habitRepository = HabitsRepository.get()
    private val _baseHabits: MutableStateFlow<List<Habit>> = MutableStateFlow(emptyList())
    private val _habits: MutableStateFlow<List<Habit>> = MutableStateFlow(emptyList())
    val habits: StateFlow<List<Habit>> = _habits.asStateFlow()
    var sortingData: SortingData = SortingData(false, false)
    var titleQuery: String? = null

    init {
        viewModelScope.launch {
            habitRepository.getHabits().collect {
                _baseHabits.value = it
                _habits.value = it
                titleQuery = null
                sortingData.needSorting = false
            }
        }
    }

    fun getHabitsFlow(habitType: HabitType?): Flow<List<Habit>> =
        habits.map { habits -> habits.filter { it.type == habitType } }

    fun filterHabits(titleQuery: String?) {
        this.titleQuery = titleQuery
        var habits = _baseHabits.value

        var filteredList = habits.filter { habit ->
            (titleQuery.isNullOrEmpty() || habit.title.contains(titleQuery, true))
        }

        if (sortingData.needSorting) {
            filteredList = sortHabitsByPriority(filteredList)
        }

        _habits.value = filteredList
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
