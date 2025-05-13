package com.slouchingdog.android.domain

import com.slouchingdog.android.domain.HabitEntityHelper.Companion.baseHabitEntity
import com.slouchingdog.android.domain.HabitEntityHelper.Companion.repository
import com.slouchingdog.android.domain.usecases.DeleteHabitUseCase
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test

class DeleteHabitUseCaseTests {
    @Test
    fun deleteHabitUseCase_shouldCallDeleteHabitRepositoryFun() = runBlocking {
        DeleteHabitUseCase(repository).invoke(baseHabitEntity)
        verify { runBlocking { repository.deleteHabit(baseHabitEntity) } }
    }
}