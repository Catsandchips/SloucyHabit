package com.slouchingdog.android.slouchyhabit.di.subcomponents

import com.slouchingdog.android.slouchyhabit.presentation.ui.habit_list.FilterBottomSheetFragment
import dagger.Subcomponent

@Subcomponent
interface FilterBottomSheetSubcomponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): FilterBottomSheetSubcomponent
    }

    fun inject(filterBottomSheetFragment: FilterBottomSheetFragment)
}