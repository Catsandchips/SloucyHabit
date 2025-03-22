package com.slouchingdog.android.slouchyhabit.ui.habits_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.slouchingdog.android.slouchyhabit.data.Habit
import com.slouchingdog.android.slouchyhabit.data.HabitType
import com.slouchingdog.android.slouchyhabit.data.HabitsStorage

class HabitsListViewModel(private val habitType: HabitType?) : ViewModel() {

    private val _habits = MutableLiveData<List<Habit>>()
    val habits: LiveData<List<Habit>> = _habits

    init {
        loadHabits()
    }

    private fun loadHabits() {
        _habits.value = HabitsStorage.getHabitsWithType(habitType)
    }
}

class HabitsListViewModelFactory(private val habitType: HabitType?) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HabitsListViewModel::class.java)) {
            return HabitsListViewModel(habitType) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}