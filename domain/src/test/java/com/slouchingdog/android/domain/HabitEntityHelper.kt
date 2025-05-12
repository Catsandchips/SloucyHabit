package com.slouchingdog.android.domain

import com.slouchingdog.android.domain.entity.HabitEntity
import com.slouchingdog.android.domain.entity.HabitType
import com.slouchingdog.android.domain.entity.SyncType
import com.slouchingdog.android.domain.usecases.AddHabitDoneDateUseCase
import com.slouchingdog.android.domain.usecases.HabitListEvent
import io.mockk.mockk
import java.time.LocalDateTime
import java.time.ZoneOffset

class HabitEntityHelper {
    companion object {
        val repository = mockk<HabitRepository>(relaxed = true)
        val today = LocalDateTime.now().toInstant(ZoneOffset.UTC).epochSecond
        val yesterday = LocalDateTime.now().minusDays(1).toEpochSecond(ZoneOffset.UTC)
        private val baseHabitEntity = HabitEntity(
            id = "test",
            title = "test",
            description = "test",
            priority = 0,
            type = HabitType.GOOD,
            periodicityTimes = 1,
            periodicityDays = 1,
            color = 0,
            date = yesterday,
            doneDates = mutableListOf(yesterday),
            syncType = SyncType.NOT_NEED
        )

        suspend fun addDoneDate(
            habitType: HabitType,
            periodicityTimes: Int
        ): Pair<HabitListEvent, Int> {
            val habitEntity = baseHabitEntity.copy(
                type = habitType,
                periodicityTimes = periodicityTimes
            )

            return AddHabitDoneDateUseCase(repository)(habitEntity = habitEntity, today)
        }
    }
}