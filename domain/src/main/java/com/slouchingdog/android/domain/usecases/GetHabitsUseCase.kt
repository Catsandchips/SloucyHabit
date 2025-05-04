package com.slouchingdog.android.domain.usecases

import com.slouchingdog.android.domain.entity.HabitEntity
import com.slouchingdog.android.domain.HabitRepository
import kotlinx.coroutines.flow.Flow

class GetHabitsUseCase(private val repository: HabitRepository) {
    suspend fun execute(): Flow<List<HabitEntity>> {
        return repository.getHabits()
    }
}