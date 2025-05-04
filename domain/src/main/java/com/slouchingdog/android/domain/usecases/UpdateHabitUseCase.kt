package com.slouchingdog.android.domain.usecases

import com.slouchingdog.android.domain.entity.HabitEntity
import com.slouchingdog.android.domain.HabitRepository

class UpdateHabitUseCase(private val repository: HabitRepository) {
    suspend fun execute(habitEntity: HabitEntity) {
        repository.updateHabit(habitEntity)
    }
}