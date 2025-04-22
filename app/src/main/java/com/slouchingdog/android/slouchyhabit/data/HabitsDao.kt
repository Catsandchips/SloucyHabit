package com.slouchingdog.android.slouchyhabit.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitsDao {
    @Query("SELECT * FROM $HABITS_TABLE_NAME ORDER BY date DESC")
    fun getHabits(): Flow<List<HabitDBEntity>>

    @Query("SELECT * FROM $HABITS_TABLE_NAME WHERE id = :id")
    fun getHabitById(id: String): HabitDBEntity

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun addHabit(habit: HabitDBEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun addHabitsList(habits: List<HabitDBEntity>)

    @Query("DELETE FROM $HABITS_TABLE_NAME")
    suspend fun deleteAllHabits()
}