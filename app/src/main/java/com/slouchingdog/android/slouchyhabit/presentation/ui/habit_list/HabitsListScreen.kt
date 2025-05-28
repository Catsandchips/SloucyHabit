package com.slouchingdog.android.slouchyhabit.presentation.ui.habit_list

import android.widget.Toast
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LiveData
import com.slouchingdog.android.domain.entity.HabitEntity
import com.slouchingdog.android.domain.entity.HabitType
import com.slouchingdog.android.domain.usecases.HabitListEvent
import com.slouchingdog.android.domain.usecases.HabitListEventData
import com.slouchingdog.android.slouchyhabit.R
import com.slouchingdog.android.slouchyhabit.presentation.ui.habit_list.components.FilterBottomSheet
import com.slouchingdog.android.slouchyhabit.presentation.ui.habit_list.components.HabitListFABs
import com.slouchingdog.android.slouchyhabit.presentation.ui.habit_list.components.HabitListPager

@Composable
fun HabitListScreen(
    onNavigateToCreateHabit: (String?) -> Unit,
    habitListStateLiveData: LiveData<HabitListState>,
    onDeleteButtonClick: (HabitEntity) -> Unit,
    onAddDoneDateButtonClick: (HabitEntity) -> Unit,
    onSetQuery: (String) -> Unit,
    onSortButtonCheck: (SortingType) -> Unit,
    onTabClick: (HabitType) -> Unit,
    onOpenFilterFABClick: () -> Unit,
    onBottomSheetDismissRequest: () -> Unit
) {
    val habitListState by habitListStateLiveData.observeAsState(HabitListState())

    ObserveHabitListEvents(habitListState.habitListEventData)
    Scaffold(floatingActionButton = {
        HabitListFABs(
            onOpenFilterFABClick = { onOpenFilterFABClick() },
            onNavigateToCreateHabit = onNavigateToCreateHabit
        )
    }) { innerPadding ->
        HabitListPager(
            onNavigateToCreateHabit = onNavigateToCreateHabit,
            innerPadding = innerPadding,
            onDeleteButtonClick = onDeleteButtonClick,
            onTabClick = onTabClick,
            onAddDoneDateButtonClick = onAddDoneDateButtonClick,
            habits = habitListState.habitList,
            pagerState = habitListState.pagerState
        )

        if (habitListState.isBottomSheetShown) {
            FilterBottomSheet(
                onDismissRequest = { onBottomSheetDismissRequest() },
                onSetQuery = onSetQuery,
                onSortButtonCheck = onSortButtonCheck,
                titleQuery = habitListState.titleQuery,
                sortingType = habitListState.sortingType
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