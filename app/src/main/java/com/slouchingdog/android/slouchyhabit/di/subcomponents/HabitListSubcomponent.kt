package com.slouchingdog.android.slouchyhabit.di.subcomponents

import com.slouchingdog.android.slouchyhabit.presentation.ui.habit_list.HabitsListFragment
import dagger.Subcomponent

@Subcomponent
interface HabitListSubcomponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): HabitListSubcomponent
    }

    fun inject(habitsListFragment: HabitsListFragment)
}