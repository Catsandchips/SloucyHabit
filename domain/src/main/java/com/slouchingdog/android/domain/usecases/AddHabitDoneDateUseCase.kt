package com.slouchingdog.android.domain.usecases

import com.slouchingdog.android.domain.HabitRepository
import com.slouchingdog.android.domain.entity.HabitEntity

class AddHabitDoneDateUseCase(private val repository: HabitRepository) {
    suspend fun execute(habitEntity: HabitEntity, doneDate: Long) {
        repository.addHabitDoneDate(habitEntity, doneDate)
    }
}