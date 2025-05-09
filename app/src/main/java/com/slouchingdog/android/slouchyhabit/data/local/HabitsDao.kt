package com.slouchingdog.android.slouchyhabit.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.slouchingdog.android.slouchyhabit.data.entity.HABITS_TABLE_NAME
import com.slouchingdog.android.slouchyhabit.data.entity.HabitDBO
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitsDao {
    @Query("SELECT * FROM $HABITS_TABLE_NAME ORDER BY date DESC")
    fun getHabits(): Flow<List<HabitDBO>>

    @Query("SELECT * FROM $HABITS_TABLE_NAME WHERE id = :id")
    fun getHabitById(id: String): HabitDBO

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun addHabit(habit: HabitDBO)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun addHabitsList(habits: List<HabitDBO>)

    @Query("DELETE FROM $HABITS_TABLE_NAME")
    suspend fun deleteAllHabits()

    @Transaction
    suspend fun replaceHabitsList(habits: List<HabitDBO>){
        deleteAllHabits()
        addHabitsList(habits)
    }
}