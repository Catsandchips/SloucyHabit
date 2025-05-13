package com.slouchingdog.android.domain

import com.slouchingdog.android.domain.HabitEntityHelper.Companion.addDoneDate
import com.slouchingdog.android.domain.HabitEntityHelper.Companion.baseHabitEntity
import com.slouchingdog.android.domain.HabitEntityHelper.Companion.repository
import com.slouchingdog.android.domain.HabitEntityHelper.Companion.today
import com.slouchingdog.android.domain.entity.HabitType
import com.slouchingdog.android.domain.usecases.AddHabitDoneDateUseCase
import com.slouchingdog.android.domain.usecases.HabitListEvent
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class AddHabitDoneDatesUseCaseTests {
    @Test
    fun addHabitDoneDateUseCase_shouldCallAddHabitDoneDateRepositoryFun() = runBlocking {
        AddHabitDoneDateUseCase(repository).invoke(baseHabitEntity, today)
        verify { runBlocking { repository.addHabitDoneDate(baseHabitEntity, today) } }
    }

    @Test
    fun addDoneDate_toOverdoneGoodHabit_shouldReturnCorrectEvent() = runBlocking {
        assertEquals(addDoneDate(HabitType.GOOD, 1), HabitListEvent.GoodHabitDoneExcessively to 0)
    }

    @Test
    fun addDoneDate_toOverdoneBadHabit_shouldReturnCorrectEvent() = runBlocking {
        assertEquals(addDoneDate(HabitType.BAD, 1), HabitListEvent.BadHabitDoneExcessively to 0)

    }

    @Test
    fun addDoneDate_toUnderdoneGoodHabit_shouldReturnCorrectEvent() = runBlocking {
        assertEquals(addDoneDate(HabitType.GOOD, 2), HabitListEvent.GoodHabitDoneNormal to 1)
    }

    @Test
    fun addDoneDate_toUnderdoneBadHabit_shouldReturnCorrectEvent() = runBlocking {
        assertEquals(addDoneDate(HabitType.BAD, 2), HabitListEvent.BadHabitDoneNormal to 1)
    }
}