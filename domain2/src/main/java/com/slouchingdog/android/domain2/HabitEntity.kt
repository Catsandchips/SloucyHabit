package com.slouchingdog.android.domain2

import com.slouchingdog.android.common2.HabitType
import com.slouchingdog.android.common2.SyncType

data class HabitEntity(
    val id: String?,
    val title: String,
    val description: String,
    val priority: Int,
    val type: HabitType,
    val periodicityTimes: Int,
    val periodicityDays: Int,
    val color: Int,
    val date: Long,
    val doneDates: MutableList<Long>,
    val syncType: SyncType
)