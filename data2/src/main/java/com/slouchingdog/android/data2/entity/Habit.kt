package com.slouchingdog.android.data2.entity

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.slouchingdog.android.common2.HabitType

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
    val color: Int = Color.parseColor("#424242"),
    val date: Long,
    @SerializedName("done_dates")
    val doneDates: IntArray = IntArray(0)
) {
    constructor(id: String, habitDTO: HabitDTO) : this(
        id,
        habitDTO.title,
        habitDTO.description,
        habitDTO.priority,
        habitDTO.type,
        habitDTO.periodicityTimes,
        habitDTO.periodicityDays,
        habitDTO.color,
        habitDTO.date,
        habitDTO.doneDates
    )
}

data class HabitDTO(
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