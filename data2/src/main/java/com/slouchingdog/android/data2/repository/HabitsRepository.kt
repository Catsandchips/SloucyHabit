package com.slouchingdog.android.data2.repository

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import com.slouchingdog.android.common2.HabitType
import com.slouchingdog.android.data2.entity.HabitDBO
import com.slouchingdog.android.data2.entity.mapToDBO
import com.slouchingdog.android.data2.entity.mapToDTO
import com.slouchingdog.android.data2.entity.mapToDomainModel
import com.slouchingdog.android.data2.entity.mapToDomainModels
import com.slouchingdog.android.data2.local.HabitDatabase
import com.slouchingdog.android.data2.remote.HabitService
import com.slouchingdog.android.domain2.HabitEntity
import com.slouchingdog.android.domain2.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val DATABASE_NAME = "slouchyHabitDB"

class HabitsRepository(context: Context) : HabitRepository {
    private val database: HabitDatabase = Room.databaseBuilder(
        context.applicationContext, HabitDatabase::class.java, DATABASE_NAME
    ).allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    private val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
    private val retryInterceptor = RetryInterceptor()

    private val okHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(retryInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

    private val retrofit = Retrofit
        .Builder()
        .baseUrl(HabitService.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .registerTypeAdapter(HabitType::class.java, EnumTypeAdapter())
                    .setStrictness(Strictness.LENIENT)
                    .create()
            )
        ).build()
    private val service = retrofit.create(HabitService::class.java)

    override fun getHabitById(id: String): HabitEntity =
        database.habitsDao().getHabitById(id).mapToDomainModel()

    override suspend fun getHabits(): Flow<List<HabitEntity>> {
        try {
            val habits = service.getHabits()
            database.habitsDao().replaceHabitsList(habits)
        } catch (e: Exception) {
            Log.e("GET HABITS ERROR", e.toString())
        }

        return database.habitsDao().getHabits().map { it.mapToDomainModels() }
    }


    override suspend fun updateHabit(habit: HabitEntity) {
        try {
            database.habitsDao().addHabit(habit.mapToDBO())
            service.updateHabit(habit.mapToDBO())
        } catch (e: Exception) {
            Log.e("UPDATE HABIT ERROR", e.toString())
        }
    }

    override suspend fun addHabit(habit: HabitEntity) {
        try {
            val habitId = service.addHabit(habit.mapToDTO())
            val habit = HabitDBO(habitId.uid, habit.mapToDTO())
            database.habitsDao().addHabit(habit)
        } catch (e: Exception) {
            Log.e("ADD HABIT ERROR", e.toString())
        }
    }
}

