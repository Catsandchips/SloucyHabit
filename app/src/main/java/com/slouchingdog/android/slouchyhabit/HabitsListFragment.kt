package com.slouchingdog.android.slouchyhabit

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.slouchingdog.android.slouchyhabit.databinding.FragmentHabitsListBinding


private const val ARG_HABITS_TYPE = "habits type"

class HabitsListFragment() : Fragment() {
    private var habitsType: HabitType? = null
    lateinit var binding: FragmentHabitsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            habitsType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable(ARG_HABITS_TYPE, HabitType::class.java)
            } else {
                it.getSerializable(ARG_HABITS_TYPE) as HabitType?
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHabitsListBinding.inflate(layoutInflater)

        binding.habitRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter =
            HabitAdapter(HabitsStorage.getHabitsWithType(habitsType)) { habit: Habit ->
                val action = HabitsListFragmentDirections.actionGoToHabitFromList(habit)
                findNavController().navigate(action)
            }
        binding.habitRecyclerView.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance(habitsType: HabitType) =
            HabitsListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_HABITS_TYPE, habitsType)
                }
            }
    }
}