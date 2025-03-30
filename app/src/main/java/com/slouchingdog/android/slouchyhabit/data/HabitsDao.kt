package com.slouchingdog.android.slouchyhabit.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitsDao {
    @Query("SELECT * FROM $HABITS_TABLE_NAME")
    fun getHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM $HABITS_TABLE_NAME WHERE id = :id")
    fun getHabitById(id: Int): Habit

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    fun addHabit(habit: Habit)
}