package com.slouchingdog.android.domain

import com.slouchingdog.android.domain.HabitEntityHelper.Companion.addDoneDate
import com.slouchingdog.android.domain.entity.HabitType
import com.slouchingdog.android.domain.usecases.HabitListEvent
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class AddHabitDoneDatesTests {

    @Test
    fun addDoneDate_toOverdoneGoodHabit_returnCorrectEvent() = runBlocking {
        assertEquals(addDoneDate(HabitType.GOOD, 1), HabitListEvent.GoodHabitDoneExcessively to 0)
    }

    @Test
    fun addDoneDate_toOverdoneBadHabit_returnCorrectEvent() = runBlocking {
        assertEquals(addDoneDate(HabitType.BAD, 1), HabitListEvent.BadHabitDoneExcessively to 0)

    }

    @Test
    fun addDoneDate_toUnderdoneGoodHabit_returnCorrectEvent() = runBlocking {
        assertEquals(addDoneDate(HabitType.GOOD, 2), HabitListEvent.GoodHabitDoneNormal to 1)
    }

    @Test
    fun addDoneDate_toUnderdoneBadHabit_returnCorrectEvent() = runBlocking {
        assertEquals(addDoneDate(HabitType.BAD, 2), HabitListEvent.BadHabitDoneNormal to 1)
    }
}