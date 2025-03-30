package com.slouchingdog.android.slouchyhabit.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Habit::class], version = 1)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitsDao(): HabitsDao
}