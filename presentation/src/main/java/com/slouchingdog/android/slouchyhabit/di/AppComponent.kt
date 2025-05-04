package com.slouchingdog.android.slouchyhabit.di

import com.slouchingdog.android.domain2.usecases.AddHabitDoneDateUseCase
import com.slouchingdog.android.domain2.usecases.AddHabitUseCase
import com.slouchingdog.android.domain2.usecases.DeleteHabitUseCase
import com.slouchingdog.android.domain2.usecases.GetHabitByIdUseCase
import com.slouchingdog.android.domain2.usecases.GetHabitsUseCase
import com.slouchingdog.android.domain2.usecases.UpdateHabitUseCase
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, DataModule::class, DomainModule::class])
@Singleton
interface AppComponent {
    fun getGetHabitByIdUseCase(): GetHabitByIdUseCase
    fun getGetHabitsUseCase(): GetHabitsUseCase
    fun getAddHabitUseCase(): AddHabitUseCase
    fun getUpdateHabitUseCase(): UpdateHabitUseCase
    fun getDeleteHabitUseCase(): DeleteHabitUseCase
    fun getAddHabitDoneDateUseCase(): AddHabitDoneDateUseCase
}