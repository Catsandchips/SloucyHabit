package com.slouchingdog.android.domain.usecases

enum class HabitListEvent {
    BadHabitDoneNormal,
    BadHabitDoneExcessively,
    GoodHabitDoneNormal,
    GoodHabitDoneExcessively
}

data class HabitListEventData(val habitListEvent: HabitListEvent, val availableExecutionsCount: Int)