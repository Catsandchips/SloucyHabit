package com.slouchingdog.android.domain2.usecases

import com.slouchingdog.android.domain2.HabitEntity
import com.slouchingdog.android.domain2.HabitRepository

class DeleteHabitUseCase(private val repository: HabitRepository) {
    suspend fun execute(habitEntity: HabitEntity) {
        repository.deleteHabit(habitEntity)
    }
}