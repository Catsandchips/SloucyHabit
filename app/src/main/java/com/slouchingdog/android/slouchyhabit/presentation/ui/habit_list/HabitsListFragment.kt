package com.slouchingdog.android.slouchyhabit.presentation.ui.habit_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.slouchingdog.android.domain.entity.HabitEntity
import com.slouchingdog.android.domain.entity.HabitType
import com.slouchingdog.android.domain.usecases.HabitListEvent
import com.slouchingdog.android.slouchyhabit.R
import com.slouchingdog.android.slouchyhabit.presentation.compose_theme.SlouchyDarkColorScheme
import com.slouchingdog.android.slouchyhabit.presentation.compose_theme.SlouchyFontFamily
import com.slouchingdog.android.slouchyhabit.presentation.compose_theme.SlouchyTheme
import com.slouchingdog.android.slouchyhabit.presentation.ui.SlouchyHabitApplication
import com.slouchingdog.android.slouchyhabit.presentation.ui.create_habit.HABIT_ID_ARG
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject
import kotlin.getValue

class HabitsListFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: HabitsListViewModelFactory
    val viewModel: HabitsListViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        (requireActivity().application as SlouchyHabitApplication)
            .appComponent
            .getHabitListSubcomponent()
            .create()
            .inject(this)
        super.onCreate(savedInstanceState)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        observeHabitListEvents()

        val tabs = listOf(
            getString(R.string.good_habits_tab_title),
            getString(R.string.bad_habits_tab_title)
        )

        return ComposeView(requireContext()).apply {

            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                SlouchyTheme {
                    val sheetState = rememberModalBottomSheetState()
                    var showBottomSheet by remember { mutableStateOf(false) }
                    val scope = rememberCoroutineScope()

                    Scaffold(floatingActionButton = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            FloatingActionButton(
                                onClick = {
                                    showBottomSheet = true
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = getString(R.string.open_filter_button_description)
                                )
                            }

                            FloatingActionButton(
                                onClick = {
                                    findNavController().navigate(
                                        R.id.nav_create
                                    )
                                },
                                containerColor = SlouchyDarkColorScheme.primary,
                                contentColor = SlouchyDarkColorScheme.surface
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = getString(R.string.add_habit_button_description)
                                )
                            }
                        }
                    }) { innerPadding ->
                        if (showBottomSheet) {
                            ModalBottomSheet(
                                onDismissRequest = {
                                    showBottomSheet = false
                                },
                                sheetState = sheetState,
                                containerColor = SlouchyDarkColorScheme.background
                            ) {
                                BottomSheetFilters()
                            }
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(innerPadding)
                        ) {
                            val pagerState = rememberPagerState { 2 }
                            TabRow(
                                selectedTabIndex = pagerState.currentPage,
                                containerColor = SlouchyDarkColorScheme.background
                            ) {
                                tabs.forEachIndexed { index, title ->
                                    Tab(
                                        text = { Text(title) },
                                        selected = pagerState.currentPage == index,
                                        onClick = {
                                            scope.launch {
                                                pagerState.animateScrollToPage(index)
                                            }
                                        }
                                    )
                                }
                            }
                            HorizontalPager(pagerState) { page ->
                                when (page) {
                                    0 -> {
                                        HabitList(HabitType.GOOD)
                                    }

                                    1 -> {
                                        HabitList(HabitType.BAD)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun BottomSheetFilters() {
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            var text by remember { mutableStateOf(viewModel.titleQuery) }
            OutlinedTextField(
                value = text,
                onValueChange = { value ->
                    text = value
                    viewModel.titleQuery = value
                    viewModel.filterHabits()
                },
                label = { Text(getString(R.string.find_habit_hint)) },
                trailingIcon = {
                    if (text.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                text = ""
                                viewModel.titleQuery = ""
                                viewModel.filterHabits()
                            }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                var isAscChecked by remember { mutableStateOf(viewModel.sortingType == SortingType.ASC) }
                var isDescChecked by remember { mutableStateOf(viewModel.sortingType == SortingType.DESC) }


                Text(
                    text = getString(R.string.sort_by_priority_title),
                    color = SlouchyDarkColorScheme.primary,
                    fontSize = 24.sp
                )
                IconToggleButton(
                    checked = isDescChecked,
                    onCheckedChange = {
                        isDescChecked = it
                        if (it) {
                            isAscChecked = false
                            viewModel.sortingType = SortingType.DESC
                            viewModel.filterHabits()
                        } else {
                            viewModel.sortingType = SortingType.NONE
                            viewModel.filterHabits()
                        }
                    }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Sort decs"
                    )
                }

                IconToggleButton(
                    checked = isAscChecked,
                    onCheckedChange = {
                        isAscChecked = it
                        if (it) {
                            isDescChecked = false
                            viewModel.sortingType = SortingType.ASC
                            viewModel.filterHabits()
                        } else {
                            viewModel.sortingType = SortingType.NONE
                            viewModel.filterHabits()
                        }
                    }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Sort asc"
                    )
                }
            }
        }
    }

    @Composable
    fun HabitList(habitType: HabitType?) {
        val habits = viewModel.getHabitsFlow(habitType).collectAsState(mutableListOf()).value
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(habits) { habit ->
                HabitCard(habit)
            }
        }
    }

    @Composable
    fun HabitCard(habit: HabitEntity) {
        Card(
            colors = CardDefaults.cardColors(containerColor = colorResource(R.color.highlight_color)),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
            onClick = {
                val bundle = bundleOf(HABIT_ID_ARG to habit.id)
                findNavController().navigate(R.id.nav_create, bundle)
            }
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = habit.title,
                        color = colorResource(R.color.headers_color),
                        fontFamily = SlouchyFontFamily,
                        fontSize = 34.sp,
                    )
                    Text(
                        text = habit.description,
                        color = colorResource(R.color.headers_color),
                        fontFamily = SlouchyFontFamily,
                        fontSize = 24.sp,
                    )
                    HabitInfoRow(habit)
                }
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { viewModel.deleteHabit(habit) },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = getString(R.string.delete_button),
                            tint = colorResource(R.color.white)
                        )
                    }

                    IconButton(
                        onClick = { viewModel.addHabitDoneDate(habit) },
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = getString(R.string.habit_done),
                            tint = colorResource(R.color.white)
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun HabitInfoRow(habit: HabitEntity) {
        val context = LocalContext.current
        val timesCountString =
            context.resources.getQuantityString(R.plurals.times_in, habit.periodicityTimes)
        val daysCountString =
            context.resources.getQuantityString(R.plurals.days, habit.periodicityDays)

        Row {
            Text(text = habit.type.title, fontFamily = SlouchyFontFamily)
            Text(text = " • ")
            Text(text = context.resources.getStringArray(R.array.priorities_array)[habit.priority])
            Text(text = " • ")
            Text(
                text = String.format(
                    Locale.getDefault(),
                    "%d %s %d %s",
                    habit.periodicityTimes,
                    timesCountString,
                    habit.periodicityDays,
                    daysCountString
                )
            )
        }
    }

    private fun observeHabitListEvents() {
        viewModel.habitListEvent.observe(viewLifecycleOwner) { event ->
            val count = viewModel.availableExecutionsCount
            val toastMessage = when (event) {
                HabitListEvent.BadHabitDoneNormal -> "${view?.resources?.getString(R.string.underdone_bad_habit_message)} $count ${
                    getPlural(
                        count
                    )
                }"

                HabitListEvent.BadHabitDoneExcessively -> view?.resources?.getString(R.string.overdone_bad_habit_message)
                HabitListEvent.GoodHabitDoneNormal -> "${view?.resources?.getString(R.string.underdone_good_habit_message)} $count ${
                    getPlural(
                        count
                    )
                }"

                HabitListEvent.GoodHabitDoneExcessively -> view?.resources?.getString(R.string.overdone_good_habit_message)
            }

            Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun getPlural(count: Int): String {
        return resources.getQuantityString(R.plurals.times, count)
    }
}