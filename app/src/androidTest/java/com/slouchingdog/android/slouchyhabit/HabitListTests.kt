package com.slouchingdog.android.slouchyhabit

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.slouchingdog.android.slouchyhabit.pageobject.screens.CreateHabitScreen
import com.slouchingdog.android.slouchyhabit.pageobject.screens.HabitListScreen
import com.slouchingdog.android.slouchyhabit.presentation.ui.MainActivity
import org.junit.Rule
import org.junit.Test

class HabitListTests : TestCase() {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun addButton_shouldOpenCreateHabitScreen() {
        HabitListScreen.createHabitButton.click()
        CreateHabitScreen.checkEmptyFormDefaultValues(device.context)
    }
}