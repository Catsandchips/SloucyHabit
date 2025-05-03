package com.slouchingdog.android.data2.repository

import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import com.slouchingdog.android.common2.HabitType
import com.slouchingdog.android.data2.entity.HabitDTO
import com.slouchingdog.android.data2.entity.UID
import com.slouchingdog.android.data2.remote.HabitService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class Service {
    private val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
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
    private val apiClient = retrofit.create(HabitService::class.java)

    suspend fun getHabits(): Response<List<HabitDTO>> {
        return apiClient.getHabits()
    }

    suspend fun updateHabit(habitDTO: HabitDTO): Result<Unit> {
        val response = apiClient.updateHabit(habitDTO)
        if (response.isSuccessful){
            return Result.success(Unit)
        }

        return Result.failure(Exception("bad http status code: ${response.message()}"))
    }

    suspend fun addHabit(habitDTO: HabitDTO): Response<UID> {
        return apiClient.addHabit(habitDTO)
    }

    suspend fun deleteHabit(id: String): Response<Unit> {
        return apiClient.deleteHabit(UID(id))
    }
}