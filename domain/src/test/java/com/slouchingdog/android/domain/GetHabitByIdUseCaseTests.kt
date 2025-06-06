package com.slouchingdog.android.domain

import com.slouchingdog.android.domain.HabitEntityHelper.Companion.repository
import com.slouchingdog.android.domain.usecases.GetHabitByIdUseCase
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetHabitByIdUseCaseTests {
    @Test
    fun getHabitByIdUseCase_shouldCallGetHabitByIdRepositoryFun() = runTest {
        GetHabitByIdUseCase(repository).invoke("test")
        verify { runBlocking { repository.getHabitById("test") } }
    }
}