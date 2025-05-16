package com.slouchingdog.android.domain.usecases

import com.slouchingdog.android.domain.HabitRepository
import com.slouchingdog.android.domain.entity.HabitEntity

class DeleteHabitUseCase(private val repository: HabitRepository) {
    suspend operator fun invoke(habitEntity: HabitEntity) {
        repository.deleteHabit(habitEntity)
    }
}