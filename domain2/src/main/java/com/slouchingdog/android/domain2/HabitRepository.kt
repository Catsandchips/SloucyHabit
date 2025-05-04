package com.slouchingdog.android.domain2

import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    suspend fun getHabits(): Flow<List<HabitEntity>>
    suspend fun addHabit(habitEntity: HabitEntity)
    suspend fun updateHabit(habitEntity: HabitEntity)
    suspend fun deleteHabit(habitEntity: HabitEntity)
    fun getHabitById(id: String): HabitEntity
}