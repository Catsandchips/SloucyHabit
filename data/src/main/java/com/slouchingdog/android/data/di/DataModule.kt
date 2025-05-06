package com.slouchingdog.android.data.di

import android.content.Context
import com.slouchingdog.android.data.local.HabitDatabase
import com.slouchingdog.android.data.remote.HabitAPIService
import com.slouchingdog.android.data.repository.HabitsRepository
import com.slouchingdog.android.domain.HabitRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule(val context: Context) {
    @Provides
    fun provideContext(): Context = context

    @Singleton
    @Provides
    fun provideHabitRepository(
        habitAPIService: HabitAPIService,
        habitDatabase: HabitDatabase
    ): HabitRepository {
        return HabitsRepository(habitAPIService, habitDatabase)
    }
}