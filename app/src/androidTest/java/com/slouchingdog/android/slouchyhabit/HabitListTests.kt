package com.slouchingdog.android.slouchyhabit

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.slouchingdog.android.slouchyhabit.pageobject.screens.CreateHabitScreen
import com.slouchingdog.android.slouchyhabit.presentation.ui.MainActivity
import org.junit.Rule
import org.junit.Test

class HabitListTests : TestCase(kaspressoBuilder = Kaspresso.Builder.withComposeSupport()) {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun addButton_shouldOpenCreateHabitScreen() {
        composeRule.onNodeWithTag("createHabitButton").performClick()
        CreateHabitScreen.checkEmptyFormDefaultValues(device.context)
    }
}