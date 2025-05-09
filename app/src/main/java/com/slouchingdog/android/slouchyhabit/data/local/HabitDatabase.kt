package com.slouchingdog.android.slouchyhabit.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.slouchingdog.android.slouchyhabit.data.entity.HabitDBO

@Database(entities = [HabitDBO::class], version = 4)
@TypeConverters(HabitTypeConverters::class)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitsDao(): HabitsDao
}