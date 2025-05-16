package com.slouchingdog.android.slouchyhabit.pageobject.screens

import com.slouchingdog.android.slouchyhabit.R
import io.github.kakaocup.kakao.screen.Screen
import io.github.kakaocup.kakao.text.KButton

object HabitListScreen : Screen<HabitListScreen>() {
    val createHabitButton = KButton { withId(R.id.createHabitButton) }
}