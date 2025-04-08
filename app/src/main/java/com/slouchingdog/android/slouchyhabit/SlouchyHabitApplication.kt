package com.slouchingdog.android.slouchyhabit

import android.app.Application
import com.slouchingdog.android.slouchyhabit.data.HabitsRepository

class SlouchyHabitApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        HabitsRepository.initialize(this)
    }
}