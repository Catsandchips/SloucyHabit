package com.slouchingdog.android.slouchyhabit.data

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.Flow

private const val DATABASE_NAME = "slouchyHabitDB"

class HabitsRepository private constructor(context: Context) {
    private val database: HabitDatabase = Room.databaseBuilder(
        context.applicationContext,
        HabitDatabase::class.java, DATABASE_NAME
    ).allowMainThreadQueries()
        .build()

    fun getHabits(): Flow<List<Habit>> = database.habitsDao().getHabits()
    fun getHabitById(id: Int): Habit = database.habitsDao().getHabitById(id)
    suspend fun addHabit(habit: Habit) = database.habitsDao().addHabit(habit)

    companion object {
        private var INSTANCE: HabitsRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = HabitsRepository(context)
            }
        }

        fun get(): HabitsRepository {
            return INSTANCE ?: throw IllegalStateException("HabitsRepository must be initialized")
        }
    }
}