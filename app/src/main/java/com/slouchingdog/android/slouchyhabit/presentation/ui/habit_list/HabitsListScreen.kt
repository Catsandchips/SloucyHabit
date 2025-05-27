package com.slouchingdog.android.slouchyhabit.presentation.ui.habit_list

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import com.slouchingdog.android.domain.entity.HabitEntity
import com.slouchingdog.android.domain.entity.HabitType
import com.slouchingdog.android.domain.usecases.HabitListEvent
import com.slouchingdog.android.domain.usecases.HabitListEventData
import com.slouchingdog.android.slouchyhabit.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun HabitListScreen(
    onNavigateToCreateHabit: (String?) -> Unit,
    habitListStateLiveData: LiveData<HabitListState>,
    onDeleteButtonClick: (HabitEntity) -> Unit,
    onAddDoneDateButtonClick: (HabitEntity) -> Unit,
    onSetQuery: (String) -> Unit,
    onSortButtonCheck: (SortingType) -> Unit,
    onTabClick: (HabitType) -> Unit
) {
    val habitListState by habitListStateLiveData.observeAsState(HabitListState())

    ObserveHabitListEvents(habitListState.habitListEventData)
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(floatingActionButton = {
        HabitListFABs(
            onOpenFilterFABClick = { showBottomSheet = true },
            onNavigateToCreateHabit = onNavigateToCreateHabit
        )
    }) { innerPadding ->
        HabitListPager(
            onNavigateToCreateHabit = onNavigateToCreateHabit,
            innerPadding = innerPadding,
            onDeleteButtonClick = onDeleteButtonClick,
            onTabClick = onTabClick,
            onAddDoneDateButtonClick = onAddDoneDateButtonClick,
            habitsFlow = habitListState.habitListFlow
        )

        if (showBottomSheet) {
            BottomSheetFilters(
                onDismissRequest = { showBottomSheet = false },
                onSetQuery = onSetQuery,
                onSortButtonCheck = onSortButtonCheck,
                titleQuery = habitListState.titleQuery,
                sortingType = habitListState.sortingType
            )
        }
    }
}

@Composable
fun HabitListPager(
    onNavigateToCreateHabit: (String?) -> Unit,
    innerPadding: PaddingValues,
    onTabClick: (HabitType) -> Unit,
    onDeleteButtonClick: (HabitEntity) -> Unit,
    onAddDoneDateButtonClick: (HabitEntity) -> Unit,
    habitsFlow: Flow<List<HabitEntity>>
) {
    val scope = rememberCoroutineScope()
    val tabs = listOf(
        stringResource(R.string.good_habits_tab_title),
        stringResource(R.string.bad_habits_tab_title)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)
    ) {
        val pagerState = rememberPagerState { 2 }
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.background
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
                    onTabClick(HabitType.GOOD)
                }

                1 -> {
                    onTabClick(HabitType.BAD)
                }
            }
            HabitList(
                onNavigateToCreateHabit = onNavigateToCreateHabit,
                habitsFlow = habitsFlow,
                onDeleteButtonClick = onDeleteButtonClick,
                onAddDoneDateButtonClick = onAddDoneDateButtonClick
            )
        }
    }
}

@Composable
fun HabitListFABs(onOpenFilterFABClick: () -> Unit, onNavigateToCreateHabit: (String?) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        FloatingActionButton(onClick = onOpenFilterFABClick) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.open_filter_button_description)
            )
        }
        FloatingActionButton(
            onClick = { onNavigateToCreateHabit(null) },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.testTag("createHabitButton")
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add_habit_button_description)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetFilters(
    onDismissRequest: () -> Unit,
    onSetQuery: (String) -> Unit,
    onSortButtonCheck: (SortingType) -> Unit,
    titleQuery: String,
    sortingType: SortingType
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HabitsSearchField(onSetQuery = onSetQuery, titleQuery = titleQuery)
            SortHabitsRow(sortingType = sortingType, onSortButtonCheck = onSortButtonCheck)
        }
    }
}

@Composable
fun HabitsSearchField(titleQuery: String, onSetQuery: (String) -> Unit) {
    var text by remember { mutableStateOf(titleQuery) }

    OutlinedTextField(
        value = text,
        onValueChange = { value ->
            text = value
            onSetQuery(value)
        },
        label = { Text(stringResource(R.string.find_habit_hint)) },
        trailingIcon = {
            if (text.isNotEmpty()) {
                IconButton(
                    onClick = {
                        text = ""
                        onSetQuery(text)
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
}

@Composable
fun SortHabitsRow(sortingType: SortingType, onSortButtonCheck: (SortingType) -> Unit) {
    var isAscChecked by remember { mutableStateOf(sortingType == SortingType.ASC) }
    var isDescChecked by remember { mutableStateOf(sortingType == SortingType.DESC) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.sort_by_priority_title),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 24.sp,
            modifier = Modifier.padding(end = 24.dp)
        )
        IconToggleButton(
            checked = isDescChecked,
            onCheckedChange = {
                isDescChecked = it
                if (it) {
                    isAscChecked = false
                    onSortButtonCheck(SortingType.DESC)
                } else {
                    onSortButtonCheck(SortingType.NONE)
                }
            }) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
        }
        IconToggleButton(
            checked = isAscChecked,
            onCheckedChange = {
                isAscChecked = it
                if (it) {
                    isDescChecked = false
                    onSortButtonCheck(SortingType.ASC)
                } else {
                    onSortButtonCheck(SortingType.NONE)
                }
            }) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = null
            )
        }
    }
}

@Composable
fun HabitList(
    onNavigateToCreateHabit: (String?) -> Unit,
    habitsFlow: Flow<List<HabitEntity>>,
    onDeleteButtonClick: (HabitEntity) -> Unit,
    onAddDoneDateButtonClick: (HabitEntity) -> Unit
) {
    val habits = habitsFlow.collectAsState(mutableListOf()).value
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(habits) { habit ->
            HabitCard(
                onNavigateToCreateHabit = onNavigateToCreateHabit,
                habit = habit,
                onDeleteButtonClick = onDeleteButtonClick,
                onAddDoneDateButtonClick
            )
        }
    }
}

@Composable
fun HabitCard(
    onNavigateToCreateHabit: (String?) -> Unit,
    habit: HabitEntity,
    onDeleteButtonClick: (HabitEntity) -> Unit,
    onAddDoneDateButtonClick: (HabitEntity) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.highlight_color)),
        modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth(),
        onClick = {
            onNavigateToCreateHabit(habit.id)
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            HabitDescriptionColumn(habit)
            HabitModificationButtons(habit, onDeleteButtonClick, onAddDoneDateButtonClick)
        }
    }
}

@Composable
fun HabitDescriptionColumn(habit: HabitEntity) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = habit.title,
            color = colorResource(R.color.headers_color),
            fontSize = 34.sp
        )
        Text(
            text = habit.description,
            color = colorResource(R.color.headers_color),
            fontSize = 24.sp
        )
        HabitInfoRow(habit)
    }
}

@Composable
fun HabitInfoRow(habit: HabitEntity) {
    val context = LocalContext.current
    val timesCountString =
        context.resources.getQuantityString(R.plurals.times_in, habit.periodicityTimes)
    val daysCountString =
        context.resources.getQuantityString(R.plurals.days, habit.periodicityDays)

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = habit.type.title)
        Text(text = stringResource(R.string.devider_dot))
        Text(text = context.resources.getStringArray(R.array.priorities_array)[habit.priority])
        Text(text = stringResource(R.string.devider_dot))
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

@Composable
fun HabitModificationButtons(
    habit: HabitEntity,
    onDeleteButtonClick: (habit: HabitEntity) -> Unit,
    onAddDoneDateButtonClick: (habit: HabitEntity) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = { onDeleteButtonClick(habit) },
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.delete_button),
                tint = colorResource(R.color.white)
            )
        }

        IconButton(
            onClick = { onAddDoneDateButtonClick(habit) },
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = stringResource(R.string.habit_done),
                tint = colorResource(R.color.white)
            )
        }
    }
}


@Composable
private fun ObserveHabitListEvents(habitListEventData: HabitListEventData?) {
    val context = LocalContext.current
    if (habitListEventData != null) {
        val event = habitListEventData.habitListEvent
        val count = habitListEventData.availableExecutionsCount
        val toastMessage = when (event) {
            HabitListEvent.BadHabitDoneNormal -> "${stringResource(R.string.underdone_bad_habit_message)} $count ${
                getPlural(count)
            }"

            HabitListEvent.BadHabitDoneExcessively -> stringResource(R.string.overdone_bad_habit_message)
            HabitListEvent.GoodHabitDoneNormal -> "${stringResource(R.string.underdone_good_habit_message)} $count ${
                getPlural(count)
            }"

            HabitListEvent.GoodHabitDoneExcessively -> stringResource(R.string.overdone_good_habit_message)
        }

        Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
    }
}

@Composable
private fun getPlural(count: Int): String {
    return LocalContext.current.resources.getQuantityString(R.plurals.times, count)
}