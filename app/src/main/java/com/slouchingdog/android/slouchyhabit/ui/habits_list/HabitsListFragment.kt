package com.slouchingdog.android.slouchyhabit.ui.habits_list

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.slouchingdog.android.slouchyhabit.data.Habit
import com.slouchingdog.android.slouchyhabit.data.HabitType
import com.slouchingdog.android.slouchyhabit.databinding.FragmentHabitsListBinding

private const val HABIT_TYPE_PARAM = "HABIT_TYPE_PARAM"

class HabitsListFragment() : Fragment() {
    lateinit var binding: FragmentHabitsListBinding
    val habitsListViewModel: HabitsListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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
        binding = FragmentHabitsListBinding.inflate(inflater)

        binding.habitRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = HabitAdapter()
        binding.habitRecyclerView.adapter = adapter

        habitsListViewModel.getHabits(habitTypeArgument)
            .observe(viewLifecycleOwner, object : Observer<List<Habit>> {
                override fun onChanged(value: List<Habit>) {
                    adapter.updateHabits(value)
                    binding.habitRecyclerView.adapter = adapter
                }
            })

        return binding.root
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