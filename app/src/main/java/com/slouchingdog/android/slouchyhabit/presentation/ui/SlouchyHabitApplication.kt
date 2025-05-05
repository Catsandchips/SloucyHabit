package com.slouchingdog.android.slouchyhabit.presentation.ui

import android.app.Application
import com.slouchingdog.android.slouchyhabit.di.AppComponent
import com.slouchingdog.android.slouchyhabit.di.DaggerAppComponent
import com.slouchingdog.android.slouchyhabit.di.DataModule

class SlouchyHabitApplication : Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .dataModule(DataModule(this))
            .build()
    }
}