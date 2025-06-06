package com.slouchingdog.android.domain.usecases

import com.slouchingdog.android.domain.HabitRepository
import com.slouchingdog.android.domain.entity.HabitEntity

class AddHabitUseCase(private val repository: HabitRepository) {
    suspend operator fun invoke(habitEntity: HabitEntity) {
        if (habitEntity.id != null) {
            repository.updateHabit(habitEntity)
        } else {
            repository.addHabit(habitEntity)
        }
    }
}