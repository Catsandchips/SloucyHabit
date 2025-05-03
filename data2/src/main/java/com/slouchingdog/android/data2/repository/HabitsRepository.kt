package com.slouchingdog.android.data2.repository

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.slouchingdog.android.common2.SyncType
import com.slouchingdog.android.data2.entity.mapToDBO
import com.slouchingdog.android.data2.entity.mapToDBOList
import com.slouchingdog.android.data2.entity.mapToDTO
import com.slouchingdog.android.data2.entity.mapToDomainModel
import com.slouchingdog.android.data2.entity.mapToDomainModels
import com.slouchingdog.android.data2.local.HabitDatabase
import com.slouchingdog.android.domain2.HabitEntity
import com.slouchingdog.android.domain2.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATABASE_NAME = "slouchyHabitDB"

class HabitsRepository(context: Context) : HabitRepository {
    private val database: HabitDatabase = Room.databaseBuilder(
        context.applicationContext, HabitDatabase::class.java, DATABASE_NAME
    ).allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    private val service = Service()
    override fun getHabitById(id: String): HabitEntity =
        database.habitsDao().getHabitById(id).mapToDomainModel()

    override suspend fun getHabits(): Flow<List<HabitEntity>> {
        try {
            syncHabits()
        } catch (e: Exception) {
            Log.e("GET HABITS ERROR", e.toString())
        }

        return database.habitsDao().getHabits().map { it.mapToDomainModels() }
    }

    override suspend fun updateHabit(habit: HabitEntity) {
        try {
            val habitDBO = habit.mapToDBO()
            val updateHabitsResponse = service.updateHabit(habit.mapToDTO())
            if (!updateHabitsResponse.isSuccessful && habitDBO.syncType != SyncType.ADD) {
                database.habitsDao().addHabit(habitDBO.copy(syncType = SyncType.UPDATE))
            } else {
                database.habitsDao().addHabit(habit.mapToDBO())
            }
        } catch (e: Exception) {
            Log.e("UPDATE HABIT ERROR", e.toString())
        }
    }

    override suspend fun addHabit(habit: HabitEntity) {
        try {
            val addHabitResponse = service.addHabit(habit.mapToDTO().copy(id = null))
            if (addHabitResponse.isSuccessful) {
                database.habitsDao()
                    .addHabit(habit.mapToDBO().copy(id = addHabitResponse.body()!!.uid))
            } else {
                database.habitsDao().addHabit(habit.mapToDBO().copy(syncType = SyncType.ADD))
            }
        } catch (e: Exception) {
            Log.e("ADD HABIT ERROR", e.toString())
        }
    }

    suspend fun syncHabits() {
        try {
            val notSyncedHabits = database.habitsDao().getNotSyncedHabits()

            for (habit in notSyncedHabits.filter { it.syncType == SyncType.ADD }) {
                val addHabitResponse = service.addHabit(habit.mapToDTO())
                if (addHabitResponse.isSuccessful) {
                    database.habitsDao().addHabit(habit.copy(syncType = SyncType.NOT_NEED))
                }
            }

            for (habit in notSyncedHabits.filter { it.syncType == SyncType.UPDATE }) {
                val updateHabitResponse = service.updateHabit(habit.mapToDTO().copy(id = null))
                if (updateHabitResponse.isSuccessful) {
                    database.habitsDao().addHabit(habit.copy(syncType = SyncType.NOT_NEED))
                }
            }

            val getHabitsResponse = service.getHabits()
            if (getHabitsResponse.isSuccessful) {
                database.habitsDao().replaceHabitsList(getHabitsResponse.body()!!.mapToDBOList())
            }
        } catch (e: Exception) {
            Log.e("SYNCHRONIZATION HABITS ERROR", e.toString())

        }
    }
}

