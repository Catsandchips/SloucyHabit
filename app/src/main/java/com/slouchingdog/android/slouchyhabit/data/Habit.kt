package com.slouchingdog.android.slouchyhabit.data

import android.graphics.Color
import java.io.Serializable

class Habit(
    val id: Int,
    val title: String,
    val description: String,
    val type: HabitType,
    val priority: String,
    val periodicityTimes: Int,
    val periodicityDays: Int,
    val color: Int = Color.parseColor("#424242")
) : Serializable