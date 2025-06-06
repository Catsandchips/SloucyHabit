package com.slouchingdog.android.domain

import com.slouchingdog.android.domain.HabitEntityHelper.Companion.repository
import com.slouchingdog.android.domain.usecases.GetHabitsUseCase
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetHabitsUseCaseTests {
    @Test
    fun getHabitListUseCase_shouldCallGetHabitListRepositoryFun() = runTest {
        GetHabitsUseCase(repository).invoke()
        verify { runBlocking { repository.getHabits() } }
    }
}