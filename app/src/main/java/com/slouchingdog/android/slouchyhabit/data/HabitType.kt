package com.slouchingdog.android.slouchyhabit.data

import androidx.annotation.StringRes
import com.slouchingdog.android.slouchyhabit.R

enum class HabitType(
    @StringRes
    val title: Int
) {
    GOOD(R.string.good_habit_radio),
    BAD(R.string.bad_habit_radio)
}