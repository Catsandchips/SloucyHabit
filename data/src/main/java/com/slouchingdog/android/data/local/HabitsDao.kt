package com.slouchingdog.android.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.slouchingdog.android.data.entity.HABITS_TABLE_NAME
import com.slouchingdog.android.data.entity.HabitDBO
import com.slouchingdog.android.domain.entity.DELETE_SYNC
import com.slouchingdog.android.domain.entity.NOT_NEED_SYNC
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitsDao {
    @Query("SELECT * FROM $HABITS_TABLE_NAME WHERE syncType != '$DELETE_SYNC' ORDER BY date DESC")
    fun getHabits(): Flow<List<HabitDBO>>

    @Query("SELECT * FROM $HABITS_TABLE_NAME WHERE id = :id")
    fun getHabitById(id: String): HabitDBO

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun addHabit(habit: HabitDBO)

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun addHabitsList(habits: List<HabitDBO>)

    @Query("DELETE FROM $HABITS_TABLE_NAME WHERE syncType = '$NOT_NEED_SYNC'")
    suspend fun deleteAllSyncedHabits()

    @Query("DELETE FROM $HABITS_TABLE_NAME WHERE id=:id")
    suspend fun deleteHabitById(id: String)

    @Query("SELECT * FROM $HABITS_TABLE_NAME WHERE syncType != '$NOT_NEED_SYNC'")
    fun getNotSyncedHabits(): List<HabitDBO>

    @Transaction
    suspend fun replaceHabitsList(habits: List<HabitDBO>) {
        deleteAllSyncedHabits()
        addHabitsList(habits)
    }
}