package com.slouchingdog.android.slouchyhabit.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.slouchingdog.android.slouchyhabit.presentation.compose_theme.SlouchyTheme
import com.slouchingdog.android.slouchyhabit.presentation.ui.create_habit.CreateHabitScreen
import com.slouchingdog.android.slouchyhabit.presentation.ui.create_habit.CreateHabitViewModel
import com.slouchingdog.android.slouchyhabit.presentation.ui.create_habit.CreateHabitViewModelFactory
import com.slouchingdog.android.slouchyhabit.presentation.ui.habit_list.HabitListScreen
import com.slouchingdog.android.slouchyhabit.presentation.ui.habit_list.HabitsListViewModel
import com.slouchingdog.android.slouchyhabit.presentation.ui.habit_list.HabitsListViewModelFactory
import kotlinx.serialization.Serializable
import javax.inject.Inject
import kotlin.getValue

class ComposeRootFragment : Fragment() {
    @Inject
    lateinit var habitListViewModelFactory: HabitsListViewModelFactory
    val habitListViewModel: HabitsListViewModel by viewModels { habitListViewModelFactory }

    @Inject
    lateinit var createHabitViewModelFactory: CreateHabitViewModelFactory
    val createHabitViewModel: CreateHabitViewModel by viewModels { createHabitViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        (requireActivity().application as SlouchyHabitApplication)
            .appComponent
            .getComposeRootSubcomponent()
            .create()
            .inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                SlouchyTheme {
                    ComposeNavigation()
                }
            }
        }
    }

    @Serializable
    object HabitListDestination

    @Serializable
    data class CreateHabitDestination(val habitId: String? = null)

    @Composable
    fun ComposeNavigation() {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = HabitListDestination) {
            composable<HabitListDestination> {
                HabitListScreen(
                    onNavigateToCreateHabit = { habitId ->
                        navController.navigate(
                            route = CreateHabitDestination(habitId)
                        )
                    },
                    habitListStateLiveData = habitListViewModel.habitListState,
                    onDeleteButtonClick = { habit -> habitListViewModel.deleteHabit(habit) },
                    onAddDoneDateButtonClick = { habit -> habitListViewModel.addHabitDoneDate(habit) },
                    onSetQuery = { titleQuery -> habitListViewModel.setTitleQuery(titleQuery) },
                    onTabClick = { habitType -> habitListViewModel.setHabitListType(habitType) },
                    onSortButtonCheck = { sortingType ->
                        habitListViewModel.setSortingType(
                            sortingType
                        )
                    })
            }
            composable<CreateHabitDestination> { backStackEntry ->
                val createHabitDestination: CreateHabitDestination = backStackEntry.toRoute()
                CreateHabitScreen(habitId = createHabitDestination.habitId)
            }
        }
    }
}