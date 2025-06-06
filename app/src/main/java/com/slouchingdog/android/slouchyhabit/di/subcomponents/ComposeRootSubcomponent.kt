package com.slouchingdog.android.slouchyhabit.di.subcomponents

import com.slouchingdog.android.slouchyhabit.presentation.ui.ComposeRootFragment
import dagger.Subcomponent

@Subcomponent
interface ComposeRootSubcomponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): ComposeRootSubcomponent
    }

    fun inject(composeRootFragment: ComposeRootFragment)
}