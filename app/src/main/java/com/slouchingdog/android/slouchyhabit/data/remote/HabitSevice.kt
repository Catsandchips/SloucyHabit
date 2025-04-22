package com.slouchingdog.android.slouchyhabit.data.remote

import com.slouchingdog.android.slouchyhabit.data.HabitDBO
import com.slouchingdog.android.slouchyhabit.data.HabitDTO
import com.slouchingdog.android.slouchyhabit.data.UID
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface HabitService {
    @GET("habit")
    suspend fun getHabits(): List<HabitDBO>

    @PUT("habit")
    suspend fun updateHabit(@Body habit: HabitDBO)

    @PUT("habit")
    suspend fun addHabit(@Body habit: HabitDTO): UID

    companion object {
        const val BASE_URL = "https://droid-test-server.doubletapp.ru/api/"
    }
}
