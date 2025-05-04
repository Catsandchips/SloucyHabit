package com.slouchingdog.android.domain.usecases

import com.slouchingdog.android.domain.entity.HabitEntity
import com.slouchingdog.android.domain.HabitRepository

class GetHabitByIdUseCase(private val repository: HabitRepository) {
    fun execute(id: String): HabitEntity {
        return repository.getHabitById(id)
    }
}