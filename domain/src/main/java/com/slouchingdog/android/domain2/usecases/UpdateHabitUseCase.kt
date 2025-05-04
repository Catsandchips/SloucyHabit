package com.slouchingdog.android.domain2.usecases

import com.slouchingdog.android.domain2.HabitEntity
import com.slouchingdog.android.domain2.HabitRepository

class UpdateHabitUseCase(private val repository: HabitRepository) {
    suspend fun execute(habitEntity: HabitEntity) {
        repository.updateHabit(habitEntity)
    }
}