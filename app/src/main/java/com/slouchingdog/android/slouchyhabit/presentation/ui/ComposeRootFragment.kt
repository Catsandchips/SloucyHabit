package com.slouchingdog.android.slouchyhabit.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.LocalActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.slouchingdog.android.slouchyhabit.R
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
    lateinit var createHabitViewModel: CreateHabitViewModel
    private var appBarTitle: CharSequence? = null


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
        SetAppBarState(navController)

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
                    onOpenFilterFABClick = { habitListViewModel.onOpenFilterFABClick() },
                    onBottomSheetDismissRequest = { habitListViewModel.onBottomSheetDismissRequest() },
                    onSortButtonCheck = { sortingType ->
                        habitListViewModel.setSortingType(
                            sortingType
                        )
                    })
            }
            composable<CreateHabitDestination> { backStackEntry ->
                val createHabitDestination: CreateHabitDestination = backStackEntry.toRoute()
                createHabitViewModel = viewModel(factory = createHabitViewModelFactory.apply {
                    habitId = createHabitDestination.habitId
                })
                CreateHabitScreen(
                    habitScreenStateLiveData = createHabitViewModel.habitScreenState,
                    onTitleChange = { title -> createHabitViewModel.onTitleChange(title) },
                    onDescriptionChange = { description ->
                        createHabitViewModel.onDescriptionChange(
                            description
                        )
                    },
                    onPrioritySelection = { priority ->
                        createHabitViewModel.onPriorityChange(
                            priority
                        )
                    },
                    onPrioritySelectorExpandedChange = { createHabitViewModel.onPrioritySelectionExpandedChange() },
                    onDismissPriorityRequest = { createHabitViewModel.onDismissPriorityRequest() },
                    onTypeSelected = { type -> createHabitViewModel.onTypeChange(type) },
                    onTimesChange = { times -> createHabitViewModel.onPeriodicityTimesChange(times) },
                    onDaysChange = { days -> createHabitViewModel.onPeriodicityDaysChange(days) },
                    onSaveButtonClick = {
                        createHabitViewModel.onSaveButtonClick()
                        navController.popBackStack()
                    }
                )
            }
        }
    }

    @Composable
    fun SetAppBarState(navController: NavHostController) {
        val currentBackStackEntry = navController.currentBackStackEntryAsState().value
        val args = currentBackStackEntry?.toRoute<CreateHabitDestination>()
        val currentDestination = currentBackStackEntry?.destination?.route
        val createHabitDestination = CreateHabitDestination::class.qualifiedName
        val activity = (LocalActivity.current as? AppCompatActivity)
        val context = LocalContext.current

        LaunchedEffect(currentDestination) {
            if (currentDestination != null && currentDestination.contains(createHabitDestination!!)) {
                activity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
                appBarTitle = activity?.supportActionBar?.title
                activity?.supportActionBar?.title =
                    if (args?.habitId != null) context.getString(R.string.edit_habit_title)
                    else context.getString(R.string.create_habit_title)

            } else {
                activity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
                if (appBarTitle != null) {
                    activity?.supportActionBar?.title = appBarTitle
                }
            }
        }
    }
}