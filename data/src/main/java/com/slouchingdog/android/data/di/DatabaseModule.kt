package com.slouchingdog.android.data.di

import android.content.Context
import androidx.room.Room
import com.slouchingdog.android.data.local.HabitDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

private const val DATABASE_NAME = "slouchyHabitDB"

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(context: Context): HabitDatabase {
        return Room.databaseBuilder(
            context.applicationContext, HabitDatabase::class.java, DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }
}