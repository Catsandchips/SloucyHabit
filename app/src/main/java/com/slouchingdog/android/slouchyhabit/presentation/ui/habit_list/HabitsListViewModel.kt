package com.slouchingdog.android.slouchyhabit.presentation.ui.habit_list

import androidx.compose.foundation.pager.PagerState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.slouchingdog.android.domain.entity.HabitEntity
import com.slouchingdog.android.domain.entity.HabitType
import com.slouchingdog.android.domain.usecases.AddHabitDoneDateUseCase
import com.slouchingdog.android.domain.usecases.DeleteHabitUseCase
import com.slouchingdog.android.domain.usecases.GetHabitsUseCase
import com.slouchingdog.android.domain.usecases.HabitListEventData
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

class HabitsListViewModel @Inject constructor(
    private val getHabitsUseCase: GetHabitsUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase,
    private val addHabitDoneDateUseCase: AddHabitDoneDateUseCase
) : ViewModel() {
    private var _baseHabits: List<HabitEntity> = mutableListOf()
    private var _habitListState: MutableLiveData<HabitListState> = MutableLiveData(HabitListState())
    val habitListState: LiveData<HabitListState> = _habitListState

    init {
        viewModelScope.launch {
            getHabitsUseCase().collect {
                _baseHabits = it
                _habitListState.value = _habitListState.value!!.copy(
                    titleQuery = "",
                    sortingType = SortingType.NONE
                )
                filterHabits()
            }
        }
    }

    private fun filterHabits() {
        var filteredList = _baseHabits.filter { habit ->
            (habit.type == habitListState.value?.habitType && (_habitListState.value!!.titleQuery.isEmpty() || habit.title.contains(
                _habitListState.value!!.titleQuery,
                true
            )))
        }
        _habitListState.value =
            _habitListState.value!!.copy(habitList = sortHabitsByPriority(filteredList))
    }

    fun setHabitListType(habitType: HabitType) {
        _habitListState.value = _habitListState.value!!.copy(habitType = habitType)
        filterHabits()
    }

    fun setSortingType(sortingType: SortingType) {
        _habitListState.value = _habitListState.value!!.copy(sortingType = sortingType)
        filterHabits()
    }

    fun setTitleQuery(titleQuery: String) {
        _habitListState.value = _habitListState.value!!.copy(titleQuery = titleQuery)
        filterHabits()
    }

    fun deleteHabit(habitEntity: HabitEntity) {
        viewModelScope.launch {
            deleteHabitUseCase(habitEntity)
        }
    }

    fun addHabitDoneDate(habitEntity: HabitEntity) {
        viewModelScope.launch {
            val eventData: HabitListEventData = addHabitDoneDateUseCase(
                habitEntity,
                LocalDateTime
                    .now()
                    .toEpochSecond(ZoneOffset.UTC)
            )

            _habitListState.value = _habitListState.value!!.copy(habitListEventData = eventData)
        }
    }

    fun onOpenFilterFABClick() {
        _habitListState.value = _habitListState.value!!.copy(isBottomSheetShown = true)
    }

    fun onBottomSheetDismissRequest() {
        _habitListState.value = _habitListState.value!!.copy(isBottomSheetShown = false)
    }

    private fun sortHabitsByPriority(habits: List<HabitEntity>): List<HabitEntity> {
        return when (_habitListState.value!!.sortingType) {
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

data class HabitListState(
    val habitType: HabitType = HabitType.GOOD,
    val habitList: List<HabitEntity> = emptyList(),
    val sortingType: SortingType = SortingType.NONE,
    val titleQuery: String = "",
    val habitListEventData: HabitListEventData? = null,
    val isBottomSheetShown: Boolean = false,
    val pagerState: PagerState = PagerState(pageCount = { 2 })
)

enum class SortingType() {
    ASC,
    DESC,
    NONE
}
