package com.slouchingdog.android.data2.entity

import com.google.gson.annotations.SerializedName

data class DoneDate(
    val date: Long,
    @SerializedName("habit_uid")
    val habitId: String
)