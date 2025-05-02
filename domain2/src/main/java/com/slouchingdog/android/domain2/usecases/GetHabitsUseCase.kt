package com.slouchingdog.android.domain2.usecases

import com.slouchingdog.android.domain2.HabitEntity
import com.slouchingdog.android.domain2.HabitRepository
import kotlinx.coroutines.flow.Flow

class GetHabitsUseCase(private val repository: HabitRepository) {
    suspend fun execute(): Flow<List<HabitEntity>> {
        return repository.getHabits()
    }
}