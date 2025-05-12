package com.slouchingdog.android.domain.usecases

import com.slouchingdog.android.domain.HabitRepository
import com.slouchingdog.android.domain.entity.HabitEntity
import com.slouchingdog.android.domain.entity.HabitType
import java.time.LocalDateTime
import java.time.ZoneOffset

class AddHabitDoneDateUseCase(private val repository: HabitRepository) {
    suspend fun execute(habitEntity: HabitEntity, doneDate: Long): Pair<HabitListEvent, Int> {
        repository.addHabitDoneDate(habitEntity, doneDate)

        var availableExecutionsCount: Int = 0
        val periodStart =
            LocalDateTime
                .now()
                .minusDays(habitEntity.periodicityDays.toLong())
                .toEpochSecond(ZoneOffset.UTC)

        val doneInPeriod = habitEntity.doneDates.filter { it >= periodStart }

        val event = if (doneInPeriod.size < habitEntity.periodicityTimes) {
            availableExecutionsCount = habitEntity.periodicityTimes - doneInPeriod.size
            if (habitEntity.type == HabitType.GOOD) {
                HabitListEvent.GoodHabitDoneNormal
            } else {
                HabitListEvent.BadHabitDoneNormal
            }
        } else {
            if (habitEntity.type == HabitType.GOOD) {
                HabitListEvent.GoodHabitDoneExcessively
            } else {
                HabitListEvent.BadHabitDoneExcessively
            }
        }

        return event to availableExecutionsCount
    }
}