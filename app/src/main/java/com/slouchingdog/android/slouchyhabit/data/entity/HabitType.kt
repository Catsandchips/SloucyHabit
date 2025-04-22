package com.slouchingdog.android.slouchyhabit.data.entity

import androidx.annotation.StringRes
import com.google.gson.annotations.JsonAdapter
import com.slouchingdog.android.slouchyhabit.R
import com.slouchingdog.android.slouchyhabit.data.repository.EnumTypeAdapter

@JsonAdapter(EnumTypeAdapter::class)
enum class HabitType(
    @StringRes
    val title: Int
) {
    GOOD(R.string.good_habit_radio),
    BAD(R.string.bad_habit_radio)
}