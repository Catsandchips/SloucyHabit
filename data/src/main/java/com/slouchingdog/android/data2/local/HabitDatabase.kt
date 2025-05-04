package com.slouchingdog.android.data2.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.slouchingdog.android.data2.entity.HabitDBO

@Database(entities = [HabitDBO::class], version = 6)
@TypeConverters(HabitTypeConverters::class)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitsDao(): HabitsDao
}