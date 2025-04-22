package com.slouchingdog.android.slouchyhabit.data.repository

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import com.slouchingdog.android.slouchyhabit.data.entity.HabitDBO
import com.slouchingdog.android.slouchyhabit.data.entity.HabitDTO
import com.slouchingdog.android.slouchyhabit.data.local.HabitDatabase
import com.slouchingdog.android.slouchyhabit.data.remote.HabitService
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val DATABASE_NAME = "slouchyHabitDB"

class HabitsRepository private constructor(context: Context) {
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
                    .setStrictness(Strictness.LENIENT)
                    .create()
            )
        ).build()
    private val service = retrofit.create(HabitService::class.java)

    fun getHabitById(id: String): HabitDBO = database.habitsDao().getHabitById(id)

    suspend fun getHabits(): Flow<List<HabitDBO>> {
        try {
            val habits = service.getHabits()
            database.habitsDao().replaceHabitsList(habits)
        } catch (e: Exception) {
            Log.e("GET HABITS ERROR", e.toString())
        }

        return database.habitsDao().getHabits()
    }


    suspend fun updateHabit(habit: HabitDBO) {
        try {
            database.habitsDao().addHabit(habit)
            service.updateHabit(habit)
        } catch (e: Exception) {
            Log.e("UPDATE HABIT ERROR", e.toString())
        }
    }

    suspend fun addHabit(habitDTO: HabitDTO) {
        try {
            val habitId = service.addHabit(habitDTO)
            val habit = HabitDBO(habitId.uid, habitDTO)
            database.habitsDao().addHabit(habit)
        } catch (e: Exception) {
            Log.e("ADD HABIT ERROR", e.toString())
        }
    }

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