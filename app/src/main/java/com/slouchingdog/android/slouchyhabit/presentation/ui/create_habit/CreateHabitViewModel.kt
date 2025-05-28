package com.slouchingdog.android.slouchyhabit.presentation.ui.create_habit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.slouchingdog.android.domain.entity.HabitEntity
import com.slouchingdog.android.domain.entity.HabitType
import com.slouchingdog.android.domain.entity.SyncType
import com.slouchingdog.android.domain.usecases.AddHabitUseCase
import com.slouchingdog.android.domain.usecases.GetHabitByIdUseCase
import com.slouchingdog.android.slouchyhabit.ui.create_habit.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

class CreateHabitViewModel @Inject constructor(
    val addHabitUseCase: AddHabitUseCase,
    val getHabitByIdUseCase: GetHabitByIdUseCase,
    val habitId: String?
) : ViewModel() {
    private val _habitScreenState: MutableLiveData<HabitScreenState> =
        MutableLiveData(HabitScreenState())
    val habitScreenState: LiveData<HabitScreenState> = _habitScreenState

    init {
        if (habitId != null) {
            viewModelScope.launch {
                val habit = getHabitByIdUseCase(habitId)
                _habitScreenState.value = HabitScreenState(
                    title = habit.title,
                    description = habit.description,
                    type = habit.type,
                    priority = habit.priority,
                    periodicityTimes = habit.periodicityTimes.toString(),
                    periodicityDays = habit.periodicityDays.toString(),
                    doneDates = habit.doneDates,
                    color = habit.color,
                    syncType = habit.syncType
                )
            }
        }
    }

    fun addHabit() {
        CoroutineScope(SupervisorJob()).launch(Dispatchers.IO) {
            val habitState = _habitScreenState.value!!
            val habitEntity = HabitEntity(
                id = habitId,
                title = habitState.title,
                description = habitState.description,
                type = habitState.type,
                priority = habitState.priority,
                periodicityTimes = habitState.periodicityTimes.toInt(),
                periodicityDays = habitState.periodicityDays.toInt(),
                date = habitState.date,
                doneDates = habitState.doneDates,
                color = habitState.color,
                syncType = habitState.syncType
            )

            addHabitUseCase(habitEntity)
        }
    }

    fun onTitleChange(newTitle: String) {
        _habitScreenState.value = _habitScreenState.value!!.copy(title = newTitle)
    }

    fun onDescriptionChange(newDescription: String) {
        _habitScreenState.value = _habitScreenState.value!!.copy(description = newDescription)
    }

    fun onTypeChange(newType: HabitType) {
        _habitScreenState.value = _habitScreenState.value!!.copy(type = newType)
    }

    fun onPriorityChange(newPriority: Int) {
        _habitScreenState.value = _habitScreenState.value!!.copy(
            priority = newPriority,
            isPrioritySelectorExpanded = false
        )
    }

    fun onPrioritySelectionExpandedChange() {
        val isSelectorExpanded = _habitScreenState.value!!.isPrioritySelectorExpanded
        _habitScreenState.value =
            _habitScreenState.value!!.copy(isPrioritySelectorExpanded = !isSelectorExpanded)
    }

    fun onDismissPriorityRequest() {
        _habitScreenState.value = _habitScreenState.value!!.copy(isPrioritySelectorExpanded = false)
    }

    fun onPeriodicityTimesChange(newPeriodicityTimes: String) {
        _habitScreenState.value =
            _habitScreenState.value!!.copy(periodicityTimes = newPeriodicityTimes)
    }

    fun onPeriodicityDaysChange(newPeriodicityDays: String) {
        _habitScreenState.value =
            _habitScreenState.value!!.copy(periodicityDays = newPeriodicityDays)
    }

    fun onSaveButtonClick() {
        val createHabitEvent = SingleLiveEvent<CreateHabitEvent>()

        createHabitEvent.value =
            if (_habitScreenState.value!!.title.isEmpty() ||
                _habitScreenState.value!!.description.isEmpty() ||
                _habitScreenState.value!!.periodicityTimes.isEmpty() ||
                _habitScreenState.value!!.periodicityDays.isEmpty()
            ) {
                CreateHabitEvent.ShowSnackBarSomeFieldsEmpty
            } else {
                addHabit()
                CreateHabitEvent.SaveHabitWithCorrectData
            }

        _habitScreenState.value = _habitScreenState.value!!.copy(createHabitEvent = createHabitEvent)
    }
}

@Suppress("UNCHECKED_CAST")
class CreateHabitViewModelFactory @Inject constructor(
    val addHabitUseCase: AddHabitUseCase,
    val getHabitByIdUseCase: GetHabitByIdUseCase,
) : ViewModelProvider.Factory {
    var habitId: String? = null

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return CreateHabitViewModel(
            addHabitUseCase,
            getHabitByIdUseCase,
            habitId
        ) as T
    }
}

data class HabitScreenState(
    val title: String = "",
    val description: String = "",
    val type: HabitType = HabitType.GOOD,
    val priority: Int = 0,
    val isPrioritySelectorExpanded: Boolean = false,
    val periodicityTimes: String = "",
    val periodicityDays: String = "",
    val date: Long = LocalDateTime.now().toInstant(ZoneOffset.UTC).epochSecond,
    val doneDates: MutableList<Long> = mutableListOf<Long>(),
    val color: Int = 0,
    val syncType: SyncType = SyncType.NOT_NEED,
    val createHabitEvent: LiveData<CreateHabitEvent>? = null
)