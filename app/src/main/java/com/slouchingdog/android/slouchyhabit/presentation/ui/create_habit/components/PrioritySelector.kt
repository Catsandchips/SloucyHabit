package com.slouchingdog.android.slouchyhabit.presentation.ui.create_habit.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.slouchingdog.android.slouchyhabit.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun exposedTextFieldColors() = ExposedDropdownMenuDefaults.textFieldColors(
    unfocusedContainerColor = Color.Transparent,
    focusedContainerColor = Color.Transparent
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioritySelector(
    priority: Int,
    isPrioritySelectorExpanded: Boolean,
    onPrioritySelection: (Int) -> Unit,
    onPrioritySelectorExpandedChange: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    val options = context.resources.getStringArray(R.array.priorities_array)
    val selectedPriority = options[priority]

    Column {
        Text(
            text = stringResource(R.string.priority_title),
            fontSize = 34.sp,
            color = MaterialTheme.colorScheme.primary
        )
        ExposedDropdownMenuBox(
            expanded = false,
            onExpandedChange = { onPrioritySelectorExpandedChange() }) {
            TextField(
                modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryEditable),
                readOnly = true,
                value = selectedPriority,
                onValueChange = {},
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isPrioritySelectorExpanded) },
                colors = exposedTextFieldColors()
            )

            ExposedDropdownMenu(
                expanded = isPrioritySelectorExpanded,
                onDismissRequest = { onDismissRequest() }) {
                options.forEach { selectedPriority ->
                    DropdownMenuItem(
                        text = { Text(selectedPriority) },
                        onClick = {
                            onPrioritySelection(options.indexOf(selectedPriority))
                        }
                    )
                }
            }
        }
    }
}