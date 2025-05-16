package com.slouchingdog.android.slouchyhabit.di.subcomponents

import com.slouchingdog.android.slouchyhabit.presentation.ui.create_habit.CreateHabitFragment
import dagger.Subcomponent


@Subcomponent()
interface CreateHabitSubcomponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): CreateHabitSubcomponent
    }

    fun inject(createHabitFragment: CreateHabitFragment)
}
