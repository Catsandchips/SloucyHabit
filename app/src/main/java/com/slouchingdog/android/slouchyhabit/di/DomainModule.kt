package com.slouchingdog.android.slouchyhabit.di

import com.slouchingdog.android.domain.HabitRepository
import com.slouchingdog.android.domain.usecases.AddHabitDoneDateUseCase
import com.slouchingdog.android.domain.usecases.AddHabitUseCase
import com.slouchingdog.android.domain.usecases.DeleteHabitUseCase
import com.slouchingdog.android.domain.usecases.GetHabitByIdUseCase
import com.slouchingdog.android.domain.usecases.GetHabitsUseCase
import com.slouchingdog.android.domain.usecases.UpdateHabitUseCase
import dagger.Module
import dagger.Provides

@Module
class DomainModule {
    @Provides
    fun provideAddHabitUseCase(repository: HabitRepository): AddHabitUseCase {
        return AddHabitUseCase(repository)
    }

    @Provides
    fun provideGetHabitByIdUseCase(repository: HabitRepository): GetHabitByIdUseCase {
        return GetHabitByIdUseCase(repository)
    }

    @Provides
    fun provideGetHabitsUseCase(repository: HabitRepository): GetHabitsUseCase {
        return GetHabitsUseCase(repository)
    }

    @Provides
    fun provideUpdateHabitsUseCase(repository: HabitRepository): UpdateHabitUseCase {
        return UpdateHabitUseCase(repository)
    }

    @Provides
    fun provideDeleteHabitUseCase(repository: HabitRepository): DeleteHabitUseCase {
        return DeleteHabitUseCase(repository)
    }

    @Provides
    fun provideAddHabitDoneDateUseCase(repository: HabitRepository): AddHabitDoneDateUseCase {
        return AddHabitDoneDateUseCase(repository)
    }
}