package com.slouchingdog.android.domain

import com.slouchingdog.android.domain.entity.HabitEntity
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    suspend fun getHabits(): Flow<List<HabitEntity>>
    suspend fun addHabit(habitEntity: HabitEntity)
    suspend fun updateHabit(habitEntity: HabitEntity)
    suspend fun deleteHabit(habitEntity: HabitEntity)
    suspend fun addHabitDoneDate(habitEntity: HabitEntity, doneDate: Long)
    suspend fun getHabitById(id: String): HabitEntity
}