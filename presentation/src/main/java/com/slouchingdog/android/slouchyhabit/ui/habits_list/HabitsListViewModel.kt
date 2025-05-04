package com.slouchingdog.android.slouchyhabit.ui.habits_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.slouchingdog.android.common2.HabitType
import com.slouchingdog.android.domain2.HabitEntity
import com.slouchingdog.android.domain2.usecases.DeleteHabitUseCase
import com.slouchingdog.android.domain2.usecases.GetHabitsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HabitsListViewModel(
    val getHabitsUseCase: GetHabitsUseCase,
    val deleteHabitUseCase: DeleteHabitUseCase
) : ViewModel() {
    private val _baseHabits: MutableStateFlow<List<HabitEntity>> = MutableStateFlow(emptyList())
    private var sortingData: SortingData = SortingData(false, false)
    private val _habits: MutableStateFlow<List<HabitEntity>> = MutableStateFlow(emptyList())
    val habits: StateFlow<List<HabitEntity>> = _habits.asStateFlow()
    var titleQuery: String? = null

    init {
        viewModelScope.launch {
            getHabitsUseCase.execute().collect {
                _baseHabits.value = it
                _habits.value = it
                titleQuery = null
                sortingData.needSorting = false
            }
        }
    }

    fun getHabitsFlow(habitType: HabitType?): Flow<List<HabitEntity>> =
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

    fun deleteHabit(habitEntity: HabitEntity) {
        viewModelScope.launch {
            deleteHabitUseCase.execute(habitEntity)
        }
    }

    private fun sortHabitsByPriority(habits: List<HabitEntity>) =
        if (sortingData.sortAsc) habits.sortedBy { it.priority } else habits.sortedByDescending { it.priority }
}

@Suppress("UNCHECKED_CAST")
class HabitsListViewModelFactory(
    val getHabitsUseCase: GetHabitsUseCase,
    val deleteHabitUseCase: DeleteHabitUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HabitsListViewModel(getHabitsUseCase, deleteHabitUseCase) as T
    }
}

data class SortingData(var needSorting: Boolean, var sortAsc: Boolean)
