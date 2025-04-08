package com.slouchingdog.android.slouchyhabit.data

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

const val HABITS_TABLE_NAME = "habits"

@Entity(tableName = HABITS_TABLE_NAME)
class Habit(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val title: String,
    val description: String,
    val type: HabitType,
    val priority: Int,
    val periodicityTimes: Int,
    val periodicityDays: Int,
    val color: Int = Color.parseColor("#424242")
)