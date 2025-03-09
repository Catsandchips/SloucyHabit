package com.slouchingdog.android.slouchyhabit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.slouchingdog.android.slouchyhabit.databinding.FragmentHabitsListBinding

class HabitsListFragment(val habitsType: HabitType) : Fragment() {
    lateinit var binding: FragmentHabitsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHabitsListBinding.inflate(layoutInflater)

        binding.habitRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = HabitAdapter(HabitsStorage.getHabitsWithType(habitsType)) { habit: Habit ->
            val action = HabitsListFragmentDirections.actionGoToHabitFromList(habit)
            findNavController().navigate(action)
        }
        binding.habitRecyclerView.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabCreateHabit.setOnClickListener {
            val action = HabitsListFragmentDirections.actionGoToHabitFromList(null)
            findNavController().navigate(action)
        }

        activity?.let { activity ->
            binding.habitsPagerViewPager.adapter = PagerAdapter(activity)
            TabLayoutMediator(
                binding.habitsPagerTabLayout,
                binding.habitsPagerViewPager
            ) { tab, position ->
                tab.text = "F $position"
            }.attach()
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(habitsType: HabitType) = HabitsListFragment(habitsType)
    }
}