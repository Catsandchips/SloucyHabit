package com.slouchingdog.android.slouchyhabit.di

import com.slouchingdog.android.data.di.DataModule
import com.slouchingdog.android.data.di.DatabaseModule
import com.slouchingdog.android.data.di.NetworkModule
import com.slouchingdog.android.domain.usecases.AddHabitDoneDateUseCase
import com.slouchingdog.android.domain.usecases.AddHabitUseCase
import com.slouchingdog.android.domain.usecases.DeleteHabitUseCase
import com.slouchingdog.android.domain.usecases.GetHabitByIdUseCase
import com.slouchingdog.android.domain.usecases.GetHabitsUseCase
import com.slouchingdog.android.slouchyhabit.di.subcomponents.CreateHabitSubcomponent
import com.slouchingdog.android.slouchyhabit.di.subcomponents.FilterBottomSheetSubcomponent
import com.slouchingdog.android.slouchyhabit.di.subcomponents.HabitListSubcomponent
import dagger.Component
import javax.inject.Singleton

@Component(modules = [NetworkModule::class, DatabaseModule::class, DomainModule::class, DataModule::class])
@Singleton
interface AppComponent {
    fun getGetHabitByIdUseCase(): GetHabitByIdUseCase
    fun getGetHabitsUseCase(): GetHabitsUseCase
    fun getAddHabitUseCase(): AddHabitUseCase
    fun getDeleteHabitUseCase(): DeleteHabitUseCase
    fun getAddHabitDoneDateUseCase(): AddHabitDoneDateUseCase
    fun getCreateHabitSubcomponent(): CreateHabitSubcomponent.Factory
    fun getHabitListSubcomponent(): HabitListSubcomponent.Factory
    fun getFilterBottomSheetSubcomponent(): FilterBottomSheetSubcomponent.Factory
}