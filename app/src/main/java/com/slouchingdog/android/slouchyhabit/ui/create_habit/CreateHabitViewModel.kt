package com.slouchingdog.android.slouchyhabit.ui.create_habit

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.slouchingdog.android.slouchyhabit.data.entity.HabitDBO
import com.slouchingdog.android.slouchyhabit.data.entity.HabitDTO
import com.slouchingdog.android.slouchyhabit.data.entity.HabitType
import com.slouchingdog.android.slouchyhabit.data.repository.HabitsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.text.isNullOrEmpty

class CreateHabitViewModel(val habitId: String?) : ViewModel() {
    private val habitsRepository = HabitsRepository.get()
    var habitState: HabitState = HabitState()
    private val _createHabitEvent = SingleLiveEvent<CreateHabitEvent>()
    val createHabitEvent: LiveData<CreateHabitEvent> = _createHabitEvent

    init {
        if (habitId != null) {
            val habit = habitsRepository.getHabitById(habitId)
            habitState = HabitState(
                habit.title,
                habit.description,
                habit.type,
                habit.priority,
                habit.periodicityTimes,
                habit.periodicityDays
            )

            _createHabitEvent.value = CreateHabitEvent.PrefillFormWithPassedHabit
        }
    }

    fun addHabit() {
        CoroutineScope(SupervisorJob()).launch(Dispatchers.IO) {
            if (habitId != null) {
                val habit = HabitDBO(
                    id = habitId,
                    title = habitState.title,
                    description = habitState.description,
                    type = habitState.type,
                    priority = habitState.priority,
                    periodicityTimes = habitState.periodicityTimes,
                    periodicityDays = habitState.periodicityDays,
                    date = habitState.date
                )

                habitsRepository.updateHabit(habit)
            } else {
                val habitDTO = HabitDTO(
                    title = habitState.title,
                    description = habitState.description,
                    type = habitState.type,
                    priority = habitState.priority,
                    periodicityTimes = habitState.periodicityTimes,
                    periodicityDays = habitState.periodicityDays,
                    date = habitState.date
                )

                habitsRepository.addHabit(habitDTO)
            }
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
class CreateHabitViewModelFactory(
    private val habitId: String?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CreateHabitViewModel(habitId) as T
    }
}

data class HabitState(
    val title: String = "",
    val description: String = "",
    val type: HabitType = HabitType.GOOD,
    val priority: Int = 0,
    val periodicityTimes: Int = 0,
    val periodicityDays: Int = 0,
    val date: Long = Date().time
)