package com.slouchingdog.android.data2.remote

import com.slouchingdog.android.data2.entity.HabitDBO
import com.slouchingdog.android.data2.entity.HabitDTO
import com.slouchingdog.android.data2.entity.UID
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface HabitService {
    @GET("habit")
    suspend fun getHabits(): Response<List<HabitDTO>>

    @PUT("habit")
    suspend fun updateHabit(@Body habit: HabitDTO): Response<Unit>

    @PUT("habit")
    suspend fun addHabit(@Body habit: HabitDTO): Response<UID>

    companion object {
        const val BASE_URL = "https://droid-test-server.doubletapp.ru/api/"
    }
}
