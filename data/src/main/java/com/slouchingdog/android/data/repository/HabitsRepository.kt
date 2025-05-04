package com.slouchingdog.android.data.repository

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.slouchingdog.android.data.entity.DoneDate
import com.slouchingdog.android.data.entity.mapToDBO
import com.slouchingdog.android.data.entity.mapToDBOList
import com.slouchingdog.android.data.entity.mapToDTO
import com.slouchingdog.android.data.entity.mapToDomainModel
import com.slouchingdog.android.data.entity.mapToDomainModels
import com.slouchingdog.android.data.local.HabitDatabase
import com.slouchingdog.android.domain.HabitRepository
import com.slouchingdog.android.domain.entity.HabitEntity
import com.slouchingdog.android.domain.entity.SyncType
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
        val habitDBO = habit.mapToDBO()
        val updateHabitsResponse = service.updateHabit(habit.mapToDTO())
        if (!updateHabitsResponse.isSuccess && habitDBO.syncType != SyncType.ADD) {
            database.habitsDao().addHabit(habitDBO.copy(syncType = SyncType.UPDATE))
        } else {
            database.habitsDao().addHabit(habitDBO)
        }
    }

    override suspend fun addHabit(habit: HabitEntity) {
        try {
            val addHabitResult = service.addHabit(habit.mapToDTO())
            if (addHabitResult.isSuccess) {
                database.habitsDao()
                    .addHabit(habit.mapToDBO().copy(id = addHabitResult.getOrThrow().uid))
            } else {
                database.habitsDao().addHabit(habit.mapToDBO().copy(syncType = SyncType.ADD))
            }
        } catch (e: Exception) {
            Log.e("ADD HABIT ERROR", e.stackTrace.toString())
        }
    }

    override suspend fun deleteHabit(habitEntity: HabitEntity) {
        try {
            val habitDBO = habitEntity.mapToDBO()
            val deleteHabitResult = service.deleteHabit(habitDBO.id)
            if (deleteHabitResult.isSuccess) {
                database.habitsDao().deleteHabitById(habitDBO.id)
            } else {
                database.habitsDao().addHabit(habitDBO.copy(syncType = SyncType.DELETE))
            }
        } catch (e: Exception) {
            Log.e("DELETE HABIT ERROR", e.toString())
        }
    }

    override suspend fun addHabitDoneDate(habitEntity: HabitEntity, doneDate: Long) {
        try {
            val habitDBO = habitEntity.mapToDBO()
            habitDBO.doneDates.add(doneDate)
            val addDoneDateResult = service.addHabitDoneDate(DoneDate(doneDate, habitDBO.id))
            if (addDoneDateResult.isSuccess) {
                database.habitsDao().addHabit(habitDBO)
            } else {
                database.habitsDao().addHabit(habitDBO.copy(syncType = SyncType.UPDATE))
            }
        } catch (e: Exception) {
            Log.e("ADD HABIT DONE DATE ERROR", e.toString())
        }
    }

    suspend fun syncHabits() {
        try {
            val notSyncedHabits = database.habitsDao().getNotSyncedHabits()
            for (habit in notSyncedHabits) {
                when (habit.syncType) {
                    SyncType.UPDATE -> {
                        val updateHabitResponse = service.updateHabit(habit.mapToDTO())
                        if (updateHabitResponse.isSuccess) {
                            database.habitsDao().addHabit(habit.copy(syncType = SyncType.NOT_NEED))
                        }
                    }

                    SyncType.ADD -> {
                        val addHabitResponse = service.addHabit(habit.mapToDTO())
                        if (addHabitResponse.isSuccess) {
                            database.habitsDao().addHabit(habit.copy(syncType = SyncType.NOT_NEED))
                        }
                    }

                    SyncType.DELETE -> {
                        val deleteHabitResponse = service.deleteHabit(habit.id)
                        if (deleteHabitResponse.isSuccess) {
                            database.habitsDao().deleteHabitById(habit.id)
                        }
                    }

                    SyncType.NOT_NEED -> {}
                }
            }

            val getHabitsResponse = service.getHabits()
            if (getHabitsResponse.isSuccess) {
                database.habitsDao()
                    .replaceHabitsList(getHabitsResponse.getOrThrow().mapToDBOList())
            }
        } catch (e: Exception) {
            Log.e("SYNCHRONIZATION HABITS ERROR", e.stackTrace.toString())
        }
    }
}

