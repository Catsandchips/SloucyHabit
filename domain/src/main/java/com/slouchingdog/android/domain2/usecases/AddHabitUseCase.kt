package com.slouchingdog.android.domain2.usecases

import com.slouchingdog.android.domain2.HabitEntity
import com.slouchingdog.android.domain2.HabitRepository

class AddHabitUseCase(private val repository: HabitRepository) {
    suspend fun execute(habitEntity: HabitEntity) {
        repository.addHabit(habitEntity)
    }
}