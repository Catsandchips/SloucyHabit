package com.slouchingdog.android.slouchyhabit.presentation.ui.habit_list

import android.os.Build
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.slouchingdog.android.domain.entity.HabitEntity
import com.slouchingdog.android.domain.entity.HabitType
import com.slouchingdog.android.domain.entity.SyncType
import com.slouchingdog.android.domain.usecases.HabitListEvent
import com.slouchingdog.android.slouchyhabit.R
import com.slouchingdog.android.slouchyhabit.presentation.compose_theme.SlouchyFontFamily
import com.slouchingdog.android.slouchyhabit.presentation.compose_theme.SlouchyTheme
import com.slouchingdog.android.slouchyhabit.presentation.ui.SlouchyHabitApplication
import com.slouchingdog.android.slouchyhabit.presentation.ui.create_habit.HABIT_ID_ARG
import java.util.Locale
import javax.inject.Inject
import kotlin.getValue

private const val HABIT_TYPE_PARAM = "HABIT_TYPE_PARAM"

class HabitsListFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: HabitsListViewModelFactory
    val viewModel: HabitsListViewModel by activityViewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        (requireActivity().application as SlouchyHabitApplication)
            .appComponent
            .getHabitListSubcomponent()
            .create()
            .inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var habitTypeArgument: HabitType? = null
        arguments?.let {
            habitTypeArgument = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable(HABIT_TYPE_PARAM, HabitType::class.java)
            } else {
                it.getSerializable(HABIT_TYPE_PARAM) as HabitType?
            }
        }

        observeHabitListEvents()

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                SlouchyTheme {
                    val habits =
                        viewModel.getHabitsFlow(habitTypeArgument).collectAsState(mutableListOf())
                    HabitList(habits.value)
                }
            }
        }
    }

    @Composable
    fun HabitList(habits: List<HabitEntity>) {
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
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = habit.title,
                        color = colorResource(R.color.headers_color),
                        fontFamily = SlouchyFontFamily,
                        fontSize = 34.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        text = habit.description,
                        color = colorResource(R.color.headers_color),
                        fontFamily = SlouchyFontFamily,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
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
                            painterResource(R.drawable.ic_clear_field_button),
                            contentDescription = getString(R.string.delete_button),
                            tint = colorResource(R.color.white)
                        )
                    }

                    IconButton(
                        onClick = { viewModel.addHabitDoneDate(habit) },
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_habit_done_button),
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

    @Composable
    @Preview
    fun PreviewList() {
        HabitList(
            mutableListOf(
                HabitEntity(
                    "1", "Title", "Description", 1, HabitType.GOOD, 1, 1, 0, 0, mutableListOf(),
                    SyncType.NOT_NEED
                ), HabitEntity(
                    "1", "Title", "Description", 1, HabitType.GOOD, 1, 1, 0, 0, mutableListOf(),
                    SyncType.NOT_NEED
                )
            )
        )
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

    companion object {
        @JvmStatic
        fun newInstance(habitsType: HabitType): HabitsListFragment =
            HabitsListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(HABIT_TYPE_PARAM, habitsType)
                }
            }
    }
}