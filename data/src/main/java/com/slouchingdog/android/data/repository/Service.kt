package com.slouchingdog.android.data.repository

import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import com.slouchingdog.android.data.entity.DoneDate
import com.slouchingdog.android.data.entity.HabitDTO
import com.slouchingdog.android.data.entity.UID
import com.slouchingdog.android.data.remote.HabitService
import com.slouchingdog.android.domain.entity.HabitType
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

    suspend fun getHabits(): Result<List<HabitDTO>> =
        getResult(apiClient.getHabits())

    suspend fun updateHabit(habitDTO: HabitDTO): Result<Unit> =
        getResult(apiClient.updateHabit(habitDTO))

    suspend fun addHabit(habitDTO: HabitDTO): Result<UID> =
        getResult(apiClient.addHabit(habitDTO))

    suspend fun deleteHabit(id: String): Result<Unit> =
        getResult(apiClient.deleteHabit(UID(id)))

    suspend fun addHabitDoneDate(doneDate: DoneDate): Result<Unit> =
        getResult(apiClient.addHabitDoneDate(doneDate))

    private fun <T> getResult(response: Response<T>): Result<T> {
        if (response.isSuccessful) {
            return Result.success(response.body()!!)
        }

        return Result.failure(Exception("bad http status code: ${response.message()}"))
    }
}