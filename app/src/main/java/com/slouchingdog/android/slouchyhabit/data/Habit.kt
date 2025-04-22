package com.slouchingdog.android.slouchyhabit.data

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val HABITS_TABLE_NAME = "habits"

@Entity(tableName = HABITS_TABLE_NAME)
data class HabitDBEntity(
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
    val color: Int = Color.parseColor("#424242"),
    val date: Long,
    @SerializedName("done_dates")
    val doneDates: IntArray? = null
) {
    constructor(id: String, habitForSave: HabitForSave) : this(
        id,
        habitForSave.title,
        habitForSave.description,
        habitForSave.priority,
        habitForSave.type,
        habitForSave.periodicityTimes,
        habitForSave.periodicityDays,
        habitForSave.color,
        habitForSave.date,
        habitForSave.doneDates
    )
}

data class HabitForSave(
    val title: String,
    val description: String,
    val priority: Int,
    val type: HabitType,
    @SerializedName("count")
    val periodicityTimes: Int,
    @SerializedName("frequency")
    val periodicityDays: Int,
    val color: Int = Color.parseColor("#424242"),
    val date: Long,
    @SerializedName("done_dates")
    val doneDates: IntArray = IntArray(0)
)