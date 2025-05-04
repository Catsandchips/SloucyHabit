package com.slouchingdog.android.data.remote

import com.slouchingdog.android.data.entity.DoneDate
import com.slouchingdog.android.data.entity.HabitDTO
import com.slouchingdog.android.data.entity.UID
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT

interface HabitService {
    @GET("habit")
    suspend fun getHabits(): Response<List<HabitDTO>>

    @PUT("habit")
    suspend fun updateHabit(@Body habit: HabitDTO): Response<Unit>

    @PUT("habit")
    suspend fun addHabit(@Body habit: HabitDTO): Response<UID>

    @HTTP(method = "DELETE", path = "habit", hasBody = true)
    suspend fun deleteHabit(@Body id: UID): Response<Unit>

    @POST("habit_done")
    suspend fun addHabitDoneDate(@Body doneDate: DoneDate): Response<Unit>

    companion object {
        const val BASE_URL = "https://droid-test-server.doubletapp.ru/api/"
    }
}
