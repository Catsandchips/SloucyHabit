package com.slouchingdog.android.slouchyhabit.presentation.ui.habit_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.slouchingdog.android.domain.entity.HabitEntity
import com.slouchingdog.android.domain.entity.HabitType
import com.slouchingdog.android.domain.usecases.AddHabitDoneDateUseCase
import com.slouchingdog.android.domain.usecases.DeleteHabitUseCase
import com.slouchingdog.android.domain.usecases.GetHabitsUseCase
import com.slouchingdog.android.domain.usecases.HabitListEvent
import com.slouchingdog.android.slouchyhabit.ui.create_habit.SingleLiveEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

class HabitsListViewModel @Inject constructor(
    private val getHabitsUseCase: GetHabitsUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase,
    private val addHabitDoneDateUseCase: AddHabitDoneDateUseCase
) : ViewModel() {
    private val _habitListEvent = SingleLiveEvent<HabitListEvent>()
    private val _baseHabits: MutableStateFlow<List<HabitEntity>> = MutableStateFlow(emptyList())
    private val _habits: MutableStateFlow<List<HabitEntity>> = MutableStateFlow(emptyList())
    var availableExecutionsCount = 0
    val habitListEvent: LiveData<HabitListEvent> = _habitListEvent
    val habits: StateFlow<List<HabitEntity>> = _habits.asStateFlow()
    var titleQuery: String = ""
    var sortingType = SortingType.NONE

    init {
        viewModelScope.launch {
            getHabitsUseCase().collect {
                _baseHabits.value = it
                _habits.value = it
                titleQuery = ""
                sortingType = SortingType.NONE
            }
        }
    }

    fun getHabitsFlow(habitType: HabitType?): Flow<List<HabitEntity>> =
        habits.map { habits -> habits.filter { it.type == habitType } }

    fun filterHabits() {
        var habits = _baseHabits.value

        var filteredList = habits.filter { habit ->
            (titleQuery.isEmpty() || habit.title.contains(titleQuery, true))
        }

        if (sortingType != SortingType.NONE) {
            filteredList = sortHabitsByPriority(filteredList)
        }

        _habits.value = filteredList
    }

    fun deleteHabit(habitEntity: HabitEntity) {
        viewModelScope.launch {
            deleteHabitUseCase(habitEntity)
        }
    }

    fun addHabitDoneDate(habitEntity: HabitEntity) {
        viewModelScope.launch {
            val eventPair = addHabitDoneDateUseCase(
                habitEntity,
                LocalDateTime
                    .now()
                    .toEpochSecond(ZoneOffset.UTC)
            )
            availableExecutionsCount = eventPair.second
            _habitListEvent.value = eventPair.first
        }
    }

    private fun sortHabitsByPriority(habits: List<HabitEntity>): List<HabitEntity> {
        return when (sortingType) {
            SortingType.ASC -> habits.sortedBy { it.priority }
            SortingType.DESC -> habits.sortedByDescending { it.priority }
            SortingType.NONE -> habits

        }
    }

}

@Suppress("UNCHECKED_CAST")
class HabitsListViewModelFactory @Inject constructor(
    val getHabitsUseCase: GetHabitsUseCase,
    val deleteHabitUseCase: DeleteHabitUseCase,
    val addHabitDoneDateUseCase: AddHabitDoneDateUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HabitsListViewModel(
            getHabitsUseCase,
            deleteHabitUseCase,
            addHabitDoneDateUseCase
        ) as T
    }
}

enum class SortingType() {
    ASC,
    DESC,
    NONE
}
