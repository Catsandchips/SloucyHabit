package com.slouchingdog.android.slouchyhabit.di

import android.content.Context
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
    fun provideHabitRepository(context: Context): HabitRepository {
        return HabitsRepository(context = context)
    }
}