package com.slouchingdog.android.slouchyhabit.ui.habits_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.slouchingdog.android.slouchyhabit.data.Habit
import com.slouchingdog.android.slouchyhabit.data.HabitType
import com.slouchingdog.android.slouchyhabit.data.HabitsStorage

class HabitsListViewModel : ViewModel() {
    private val _filteredHabits = MutableLiveData<List<Habit>>()
    val filteredHabits: LiveData<List<Habit>> = _filteredHabits

    private val _filters = MutableLiveData<HabitFilters>()
    val filters: LiveData<HabitFilters> = _filters

    init {
        HabitsStorage.habits.observeForever { habits ->
            applyFilters(habits, _filters.value)
        }
    }

    fun getHabits(habitTypeFilter: HabitType?): LiveData<List<Habit>> =
        filteredHabits.map { habits ->
            habits.filter { it.type == habitTypeFilter }
        }

    fun setFilters(filters: HabitFilters) {
        _filters.value = filters
        applyFilters(HabitsStorage.habits.value ?: emptyList(), filters)
    }

    private fun applyFilters(habits: List<Habit>, filters: HabitFilters?) {
        if (filters == null) {
            _filteredHabits.value = habits
            return
        }

        val filteredList = habits.filter { habit ->
            (filters.type == null || habit.type == filters.type) &&
                    (filters.priority.isNullOrEmpty() || habit.priority == filters.priority)
        }

        _filteredHabits.value = filteredList
    }
}

data class HabitFilters(
    val nameQuery: String? = null,
    val type: HabitType? = null,
    val priority: String? = null
)