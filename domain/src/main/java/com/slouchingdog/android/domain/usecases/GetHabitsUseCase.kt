package com.slouchingdog.android.domain.usecases

import com.slouchingdog.android.domain.HabitRepository
import com.slouchingdog.android.domain.entity.HabitEntity
import kotlinx.coroutines.flow.Flow

class GetHabitsUseCase(private val repository: HabitRepository) {
    suspend operator fun invoke(): Flow<List<HabitEntity>> {
        return repository.getHabits()
    }
}