package com.slouchingdog.android.domain

import com.slouchingdog.android.domain.HabitEntityHelper.Companion.baseHabitEntity
import com.slouchingdog.android.domain.HabitEntityHelper.Companion.repository
import com.slouchingdog.android.domain.usecases.AddHabitUseCase
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AddHabitUseCaseTests {
    @Test
    fun addHabitUseCase_withHabitWithoutId_callAddHabitRepositoryFun() = runTest {
        AddHabitUseCase(repository).invoke(baseHabitEntity)
        verify { runBlocking { repository.addHabit(baseHabitEntity) } }
    }

    @Test
    fun addHabitUseCase_withHabitWithId_callAddHabitRepositoryFun() = runTest {
        val habit = baseHabitEntity.copy(id = "test")
        AddHabitUseCase(repository).invoke(habit)
        verify { runBlocking { repository.updateHabit(habit) } }
    }
}