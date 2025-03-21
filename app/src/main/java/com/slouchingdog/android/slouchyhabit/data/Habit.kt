package com.slouchingdog.android.slouchyhabit.data

import android.graphics.Color
import java.io.Serializable
import java.util.UUID

class Habit(
    var id: UUID,
    var title: String,
    var description: String,
    var type: HabitType,
    var priority: String,
    var periodicityTimes: Int,
    var periodicityDays: Int,
    val color: Int = Color.parseColor("#424242")
) : Serializable