package com.slouchingdog.android.slouchyhabit.presentation.ui.habit_list

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.slouchingdog.android.domain.entity.HabitEntity
import com.slouchingdog.android.domain.entity.HabitType
import com.slouchingdog.android.domain.usecases.HabitListEvent
import com.slouchingdog.android.slouchyhabit.R
import com.slouchingdog.android.slouchyhabit.databinding.FragmentHabitsListBinding
import com.slouchingdog.android.slouchyhabit.presentation.ui.SlouchyHabitApplication
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val HABIT_TYPE_PARAM = "HABIT_TYPE_PARAM"

class HabitsListFragment() : Fragment() {
    lateinit var binding: FragmentHabitsListBinding
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHabitsListBinding.inflate(inflater)
        observeHabitListEvents()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var habitTypeArgument: HabitType? = null
        arguments?.let {
            habitTypeArgument = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable(HABIT_TYPE_PARAM, HabitType::class.java)
            } else {
                it.getSerializable(HABIT_TYPE_PARAM) as HabitType?
            }
        }

        binding.habitRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = HabitAdapter(
            { habit -> onDeleteButtonClick(habit) },
            { habit -> onDoneButtonClick(habit) })
        binding.habitRecyclerView.adapter = adapter

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getHabitsFlow(habitTypeArgument)
                    .collect { habits: List<HabitEntity> ->
                        adapter.updateHabits(habits)
                    }
            }
        }
    }

    fun onDeleteButtonClick(habitEntity: HabitEntity) {
        viewModel.deleteHabit(habitEntity)
    }

    fun onDoneButtonClick(habitEntity: HabitEntity) {
        viewModel.addHabitDoneDate(habitEntity)
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