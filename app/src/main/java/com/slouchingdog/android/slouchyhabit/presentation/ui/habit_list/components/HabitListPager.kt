package com.slouchingdog.android.slouchyhabit.presentation.ui.habit_list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.slouchingdog.android.domain.entity.HabitEntity
import com.slouchingdog.android.domain.entity.HabitType
import com.slouchingdog.android.slouchyhabit.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitListPager(
    onNavigateToCreateHabit: (String?) -> Unit,
    innerPadding: PaddingValues,
    onTabClick: (HabitType) -> Unit,
    onDeleteButtonClick: (HabitEntity) -> Unit,
    onAddDoneDateButtonClick: (HabitEntity) -> Unit,
    habits: List<HabitEntity>,
    pagerState: PagerState
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
        PrimaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.background
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.scrollToPage(index)
                        }
                    }
                )
            }
        }
        HorizontalPager(pagerState) { page ->
            when (page) {
                0 -> {
                    onTabClick(HabitType.GOOD)
                    HabitList(
                        onNavigateToCreateHabit = onNavigateToCreateHabit,
                        habits = habits,
                        onDeleteButtonClick = onDeleteButtonClick,
                        onAddDoneDateButtonClick = onAddDoneDateButtonClick
                    )
                }

                1 -> {
                    onTabClick(HabitType.BAD)
                    HabitList(
                        onNavigateToCreateHabit = onNavigateToCreateHabit,
                        habits = habits,
                        onDeleteButtonClick = onDeleteButtonClick,
                        onAddDoneDateButtonClick = onAddDoneDateButtonClick
                    )
                }
            }
        }
    }
}