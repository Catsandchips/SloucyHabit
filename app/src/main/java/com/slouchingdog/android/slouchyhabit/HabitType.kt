package com.slouchingdog.android.slouchyhabit

import androidx.annotation.StringRes

enum class HabitType(
    @StringRes
    val title: Int
) {
    GOOD(R.string.good_habit_radio),
    BAD(R.string.bad_habit_radio)
}