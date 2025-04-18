package com.slouchingdog.android.slouchyhabit.data.remote

import com.slouchingdog.android.slouchyhabit.data.HabitDBEntity
import com.slouchingdog.android.slouchyhabit.data.HabitForSave
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT
import java.util.UUID

interface HabitService {
    @Headers(
        
    )
    @GET("habit")
    suspend fun getHabits(): Response<List<HabitDBEntity>>

    @PUT("habit")
    suspend fun updateHabit(@Body habit: HabitDBEntity)

    @Headers("Content-Type: application/json", "accept: application/json")
    @PUT("habit")
    suspend fun addHabit(@Body habit: HabitForSave): Response<String>

    companion object {
        const val BASE_URL = "https://droid-test-server.doubletapp.ru/api/"
    }
}
