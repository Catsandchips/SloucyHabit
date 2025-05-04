package com.slouchingdog.android.domain2.usecases

import com.slouchingdog.android.domain2.HabitEntity
import com.slouchingdog.android.domain2.HabitRepository

class AddHabitDoneDateUseCase(private val repository: HabitRepository) {
    suspend fun execute(habitEntity: HabitEntity, doneDate: Long) {
        repository.addHabitDoneDate(habitEntity, doneDate)
    }
}