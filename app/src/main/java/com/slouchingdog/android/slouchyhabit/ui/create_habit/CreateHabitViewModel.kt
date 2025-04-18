package com.slouchingdog.android.slouchyhabit.ui.create_habit

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.slouchingdog.android.slouchyhabit.data.Habit
import com.slouchingdog.android.slouchyhabit.data.HabitType
import com.slouchingdog.android.slouchyhabit.data.HabitsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.text.isNullOrEmpty

class CreateHabitViewModel(habitId: Int?) : ViewModel() {
    private val habitsRepository = HabitsRepository.get()
    var habitState: HabitState = HabitState()
    private val _createHabitEvent = SingleLiveEvent<CreateHabitEvent>()
    val createHabitEvent: LiveData<CreateHabitEvent> = _createHabitEvent

    init {
        if (habitId != null && habitId != -1) {
            val habit = habitsRepository.getHabitById(habitId)
            habitState = HabitState(
                habit.id,
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
        viewModelScope.launch {
            launch(Dispatchers.IO) {
                val habit = Habit(
                    id = habitState.id,
                    title = habitState.title,
                    description = habitState.description,
                    type = habitState.type,
                    priority = habitState.priority,
                    periodicityTimes = habitState.periodicityTimes ?: 0,
                    periodicityDays = habitState.periodicityDays ?: 0
                )

                habitsRepository.addHabit(habit)
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

    fun onPeriodicityTimesChange(newPeriodicityTimes: Int?) {
        habitState = habitState.copy(periodicityTimes = newPeriodicityTimes)
    }

    fun onPeriodicityDaysChange(newPeriodicityDays: Int?) {
        habitState = habitState.copy(periodicityDays = newPeriodicityDays)
    }

    fun onSaveButtonClick() {
        _createHabitEvent.value =
            if (habitState.title.isEmpty() || habitState.description.isEmpty() || habitState.periodicityTimes == null || habitState.periodicityDays == null) {
                CreateHabitEvent.ShowSnackBarSomeFieldsEmpty
            } else {
                CreateHabitEvent.SaveHabitWithCorrectData
            }

    }

    fun getPeriodicityForPlurals(input: Editable?) =
        if (input.isNullOrEmpty()) 0 else input.toString().toInt()

    fun getPeriodicityForState(input: Editable?) =
        if (input.isNullOrEmpty()) null else input.toString().toInt()

}

@Suppress("UNCHECKED_CAST")
class CreateHabitViewModelFactory(
    private val habitId: Int?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CreateHabitViewModel(habitId) as T
    }
}

data class HabitState(
    val id: Int? = null,
    val title: String = "",
    val description: String = "",
    val type: HabitType = HabitType.GOOD,
    val priority: Int = 0,
    val periodicityTimes: Int? = null,
    val periodicityDays: Int? = null
)