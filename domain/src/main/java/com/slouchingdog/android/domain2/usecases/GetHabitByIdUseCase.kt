package com.slouchingdog.android.domain2.usecases

import com.slouchingdog.android.domain2.HabitEntity
import com.slouchingdog.android.domain2.HabitRepository

class GetHabitByIdUseCase(private val repository: HabitRepository) {
    fun execute(id: String): HabitEntity {
        return repository.getHabitById(id)
    }
}