package com.slouchingdog.android.slouchyhabit.presentation.ui.create_habit

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
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
import kotlin.text.isNullOrEmpty

class CreateHabitViewModel @Inject constructor(
    val addHabitUseCase: AddHabitUseCase,
    val getHabitByIdUseCase: GetHabitByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var habitState: HabitState = HabitState()
    private val _createHabitEvent = SingleLiveEvent<CreateHabitEvent>()
    val createHabitEvent: LiveData<CreateHabitEvent> = _createHabitEvent
    val habitId = savedStateHandle.get<String>(HABIT_ID_ARG)

    init {
        if (habitId != null) {
            viewModelScope.launch {
                val habit = getHabitByIdUseCase(habitId)
                habitState = HabitState(
                    title = habit.title,
                    description = habit.description,
                    type = habit.type,
                    priority = habit.priority,
                    periodicityTimes = habit.periodicityTimes,
                    periodicityDays = habit.periodicityDays,
                    doneDates = habit.doneDates,
                    color = habit.color,
                    syncType = habit.syncType
                )
                _createHabitEvent.value = CreateHabitEvent.PrefillFormWithPassedHabit
            }
        }
    }

    fun addHabit() {
        CoroutineScope(SupervisorJob()).launch(Dispatchers.IO) {
            val habitEntity = HabitEntity(
                id = habitId,
                title = habitState.title,
                description = habitState.description,
                type = habitState.type,
                priority = habitState.priority,
                periodicityTimes = habitState.periodicityTimes,
                periodicityDays = habitState.periodicityDays,
                date = habitState.date,
                doneDates = habitState.doneDates,
                color = habitState.color,
                syncType = habitState.syncType
            )

            addHabitUseCase(habitEntity)
        }
    }

    fun onTitleChange(newTitle: String) {
        habitState = habitState.copy(title = newTitle)
    }

    fun onDescriptionChange(newDescription: String) {
        habitState = habitState.copy(description = newDescription)
    }

    fun onTypeChange(newType: HabitType) {
        habitState = habitState.copy(type = newType)
    }

    fun onPriorityChange(newPriority: Int) {
        habitState = habitState.copy(priority = newPriority)
    }

    fun onPeriodicityTimesChange(newPeriodicityTimes: Int) {
        habitState = habitState.copy(periodicityTimes = newPeriodicityTimes)
    }

    fun onPeriodicityDaysChange(newPeriodicityDays: Int) {
        habitState = habitState.copy(periodicityDays = newPeriodicityDays)
    }

    fun onSaveButtonClick() {
        _createHabitEvent.value =
            if (habitState.title.isEmpty() || habitState.description.isEmpty() || habitState.periodicityTimes == 0 || habitState.periodicityDays == 0) {
                CreateHabitEvent.ShowSnackBarSomeFieldsEmpty
            } else {
                addHabit()
                CreateHabitEvent.SaveHabitWithCorrectData
            }
    }

    fun getPeriodicityForPlurals(input: Editable?): Int =
        if (input.isNullOrEmpty()) 0 else input.toString().toInt()

    fun getPeriodicityForState(input: Editable?): Int =
        if (input.isNullOrEmpty()) 0 else input.toString().toInt()
}

@Suppress("UNCHECKED_CAST")
class CreateHabitViewModelFactory @Inject constructor(
    val addHabitUseCase: AddHabitUseCase,
    val getHabitByIdUseCase: GetHabitByIdUseCase,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return CreateHabitViewModel(
            addHabitUseCase,
            getHabitByIdUseCase,
            extras.createSavedStateHandle()
        ) as T
    }
}

data class HabitState(
    val title: String = "",
    val description: String = "",
    val type: HabitType = HabitType.GOOD,
    val priority: Int = 0,
    val periodicityTimes: Int = 0,
    val periodicityDays: Int = 0,
    val date: Long = LocalDateTime.now().toInstant(ZoneOffset.UTC).epochSecond,
    val doneDates: MutableList<Long> = mutableListOf<Long>(),
    val color: Int = 0,
    val syncType: SyncType = SyncType.NOT_NEED
)