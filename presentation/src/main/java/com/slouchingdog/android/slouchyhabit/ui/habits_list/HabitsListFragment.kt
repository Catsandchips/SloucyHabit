package com.slouchingdog.android.slouchyhabit.ui.habits_list

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.slouchingdog.android.common2.HabitType
import com.slouchingdog.android.domain2.HabitEntity
import com.slouchingdog.android.slouchyhabit.SlouchyHabitApplication
import com.slouchingdog.android.slouchyhabit.databinding.FragmentHabitsListBinding
import com.slouchingdog.android.slouchyhabit.di.AppComponent
import kotlinx.coroutines.launch

private const val HABIT_TYPE_PARAM = "HABIT_TYPE_PARAM"

class HabitsListFragment() : Fragment() {
    lateinit var binding: FragmentHabitsListBinding
    val appComponent: AppComponent by lazy { (requireActivity().application as SlouchyHabitApplication).appComponent }
    val viewModel: HabitsListViewModel by activityViewModels {
        HabitsListViewModelFactory(
            appComponent.getGetHabitsUseCase(),
            appComponent.getDeleteHabitUseCase()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHabitsListBinding.inflate(inflater)
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
        val adapter = HabitAdapter { habitId -> onDeleteButtonClick(habitId) }
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

    fun onDeleteButtonClick(habitId: String) {
        viewModel.deleteHabit(habitId)
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