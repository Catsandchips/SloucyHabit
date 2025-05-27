package com.slouchingdog.android.slouchyhabit.presentation.ui.create_habit

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.toRoute
import com.slouchingdog.android.domain.entity.HabitEntity
import com.slouchingdog.android.domain.entity.HabitType
import com.slouchingdog.android.domain.entity.SyncType
import com.slouchingdog.android.domain.usecases.AddHabitUseCase
import com.slouchingdog.android.domain.usecases.GetHabitByIdUseCase
import com.slouchingdog.android.slouchyhabit.presentation.ui.ComposeRootFragment.CreateHabitDestination
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
    savedStateHandle: SavedStateHandle,
    val habitId: String?
) : ViewModel() {
    private val _habitState: MutableLiveData<HabitState> = MutableLiveData(HabitState())
    val habitState: LiveData<HabitState> = _habitState
    private val _createHabitEvent = SingleLiveEvent<CreateHabitEvent>()
    val createHabitEvent: LiveData<CreateHabitEvent> = _createHabitEvent

    init {
        if (habitId != null) {
            viewModelScope.launch {
                val habit = getHabitByIdUseCase(habitId)
                _habitState.value = HabitState(
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
                _createHabitEvent.value = CreateHabitEvent.PrefillFormWithPassedHabit
            }
        }
    }

    fun addHabit() {
        CoroutineScope(SupervisorJob()).launch(Dispatchers.IO) {
            val habitState = _habitState.value!!
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
        _habitState.value = _habitState.value!!.copy(title = newTitle)
    }

    fun onDescriptionChange(newDescription: String) {
        _habitState.value = _habitState.value!!.copy(description = newDescription)
    }

    fun onTypeChange(newType: HabitType) {
        _habitState.value = _habitState.value!!.copy(type = newType)
    }

    fun onPriorityChange(newPriority: Int) {
        _habitState.value = _habitState.value!!.copy(priority = newPriority)
    }

    fun onPeriodicityTimesChange(newPeriodicityTimes: String) {
        _habitState.value = _habitState.value!!.copy(periodicityTimes = newPeriodicityTimes)
    }

    fun onPeriodicityDaysChange(newPeriodicityDays: String) {
        _habitState.value = _habitState.value!!.copy(periodicityDays = newPeriodicityDays)
    }

    fun onSaveButtonClick() {
        _createHabitEvent.value =
            if (_habitState.value!!.title.isEmpty() ||
                _habitState.value!!.description.isEmpty() ||
                _habitState.value!!.periodicityTimes.isEmpty() ||
                _habitState.value!!.periodicityDays.isEmpty()
            ) {
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
    var habitId: String? = null

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return CreateHabitViewModel(
            addHabitUseCase,
            getHabitByIdUseCase,
            extras.createSavedStateHandle(),
            habitId
        ) as T
    }
}

data class HabitState(
    val title: String = "",
    val description: String = "",
    val type: HabitType = HabitType.GOOD,
    val priority: Int = 0,
    val periodicityTimes: String = "",
    val periodicityDays: String = "",
    val date: Long = LocalDateTime.now().toInstant(ZoneOffset.UTC).epochSecond,
    val doneDates: MutableList<Long> = mutableListOf<Long>(),
    val color: Int = 0,
    val syncType: SyncType = SyncType.NOT_NEED
)