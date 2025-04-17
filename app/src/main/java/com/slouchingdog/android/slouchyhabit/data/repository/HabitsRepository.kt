package com.slouchingdog.android.slouchyhabit.data.repository

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import com.slouchingdog.android.slouchyhabit.data.HabitDBEntity
import com.slouchingdog.android.slouchyhabit.data.HabitDatabase
import com.slouchingdog.android.slouchyhabit.data.HabitForSave
import com.slouchingdog.android.slouchyhabit.data.HabitType
import com.slouchingdog.android.slouchyhabit.data.remote.HabitService
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.UUID

private const val DATABASE_NAME = "slouchyHabitDB"

class HabitsRepository private constructor(context: Context) {
    private val database: HabitDatabase = Room.databaseBuilder(
        context.applicationContext,
        HabitDatabase::class.java, DATABASE_NAME
    ).allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(HabitService.BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .setStrictness(Strictness.LENIENT)
                    .create()
            )
        )
        .build()
    private val service = retrofit.create(HabitService::class.java)


    suspend fun getHabits(): Flow<List<HabitDBEntity>> {
        try {
            val habits = service.getHabits().body()
            Log.d("HABITS FROM API", habits?.get(0).toString())
            if (habits != null)
                database.habitsDao().addHabitsList(habits)
        } catch (e: Exception) {
            Log.e("GET HABITS ERROR", e.toString())
        }

        return database.habitsDao().getHabits()
    }

    fun getHabitById(id: UUID): HabitDBEntity = database.habitsDao().getHabitById(id)
    suspend fun updateHabit(habit: HabitDBEntity) {
        try {
            database.habitsDao().addHabit(habit)
            service.updateHabit(habit)
        }
        catch (e: Exception){
            Log.e("UPDATE HABIT ERROR", e.toString())
        }
    }

    suspend fun addHabit(habitForSave: HabitForSave){
        try {
            val a = Gson().toJson(habitForSave)
            Log.e("JSON HABIT", a.toString())

            val habitId = service.addHabit(habitForSave).body()
            val habit = HabitDBEntity(UUID.fromString(habitId), habitForSave)
            database.habitsDao().addHabit(habit)
        }
        catch (e: Exception){
            Log.e("UPDATE HABIT ERROR", e.toString())
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