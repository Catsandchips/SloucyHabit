package com.slouchingdog.android.slouchyhabit.presentation.ui.habit_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slouchingdog.android.slouchyhabit.R
import com.slouchingdog.android.slouchyhabit.presentation.ui.habit_list.SortingType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    onDismissRequest: () -> Unit,
    onSetQuery: (String) -> Unit,
    onSortButtonCheck: (SortingType) -> Unit,
    titleQuery: String,
    sortingType: SortingType
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
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
    OutlinedTextField(
        value = titleQuery,
        onValueChange = { value ->
            onSetQuery(value)
        },
        label = { Text(stringResource(R.string.find_habit_hint)) },
        trailingIcon = {
            if (titleQuery.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onSetQuery("")
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
            checked = sortingType == SortingType.DESC,
            onCheckedChange = {
                if (it) {
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
            checked = sortingType == SortingType.ASC,
            onCheckedChange = {
                if (it) {
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