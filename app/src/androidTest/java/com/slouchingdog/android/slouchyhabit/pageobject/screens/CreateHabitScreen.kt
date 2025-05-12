package com.slouchingdog.android.slouchyhabit.pageobject.screens

import android.content.Context
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.withSpinnerText
import com.slouchingdog.android.slouchyhabit.R
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.screen.Screen


object CreateHabitScreen : Screen<CreateHabitScreen>() {
    val habitNameField = KEditText { withId(R.id.habitNameField) }
    val habitDescriptionField = KEditText { withId(R.id.habitDescriptionField) }
    val prioritySpinner = KView { withId(R.id.habitPrioritySpinner) }
    val goodHabitRadioButton = KView { withId(R.id.goodHabitRadioButton) }
    val badHabitRadioButton = KView { withId(R.id.badHabitRadioButton) }
    val repetitionsField = KEditText { withId(R.id.repetitionsField) }
    val daysCountField = KEditText { withId(R.id.daysCountField) }

    fun checkEmptyFormDefaultValues(context: Context) {
        habitNameField {
            isVisible()
            hasText("")
        }
        habitDescriptionField {
            isVisible()
            hasText("")
        }
        prioritySpinner {
            isVisible()
            withSpinnerText(context.resources.getStringArray(R.array.priorities_array)[0])
        }
        goodHabitRadioButton {
            isVisible()
            isChecked()
        }
        badHabitRadioButton {
            isVisible()
            isNotChecked()
        }
        repetitionsField {
            isVisible()
            hasText("")
        }
        daysCountField {
            isVisible()
            hasText("")
        }
    }
}
