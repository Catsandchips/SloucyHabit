package com.slouchingdog.android.data.remote

import com.slouchingdog.android.data.entity.DoneDate
import com.slouchingdog.android.data.entity.HabitDTO
import com.slouchingdog.android.data.entity.UID
import retrofit2.Response

class HabitAPIService(val apiClient: HabitApiClient) {
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