package com.slouchingdog.android.domain.usecases

import com.slouchingdog.android.domain.HabitRepository
import com.slouchingdog.android.domain.entity.HabitEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetHabitByIdUseCase(private val repository: HabitRepository) {
    suspend fun execute(id: String): HabitEntity = withContext(Dispatchers.IO) {
        repository.getHabitById(id)
    }
}