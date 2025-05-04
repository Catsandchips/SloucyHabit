package com.slouchingdog.android.data2.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.slouchingdog.android.common2.HabitType
import com.slouchingdog.android.common2.SyncType

const val HABITS_TABLE_NAME = "habits"

@Entity(tableName = HABITS_TABLE_NAME)
data class HabitDBO(
    @SerializedName("uid")
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val priority: Int,
    val type: HabitType,
    @SerializedName("count")
    val periodicityTimes: Int,
    @SerializedName("frequency")
    val periodicityDays: Int,
    val color: Int,
    val date: Long,
    @SerializedName("done_dates")
    val doneDates: MutableList<Long>,
    val syncType: SyncType
)

data class HabitDTO(
    @SerializedName("uid")
    val id: String?,
    val title: String,
    val description: String,
    val priority: Int,
    val type: HabitType,
    @SerializedName("count")
    val periodicityTimes: Int,
    @SerializedName("frequency")
    val periodicityDays: Int,
    val color: Int,
    val date: Long,
    @SerializedName("done_dates")
    val doneDates: MutableList<Long>
)