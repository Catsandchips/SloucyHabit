package com.slouchingdog.android.slouchyhabit.ui.habits_pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.slouchingdog.android.slouchyhabit.R
import com.slouchingdog.android.slouchyhabit.data.Habit
import com.slouchingdog.android.slouchyhabit.databinding.FragmentHabitsPagerBinding
import com.slouchingdog.android.slouchyhabit.ui.create_habit.CreateHabitFragment

class HabitsPagerFragment : Fragment() {
    lateinit var binding: FragmentHabitsPagerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHabitsPagerBinding.inflate(layoutInflater)

        activity?.let { activity ->
            updateAdapter(activity)
            TabLayoutMediator(
                binding.habitsPagerTabLayout,
                binding.habitsPagerViewPager
            ) { tab, position ->
                if (position == 0) tab.text = resources.getString(R.string.good_habits_tab_title)
                else tab.text = resources.getString(R.string.bad_habits_tab_title)
            }.attach()
        }

        binding.fabCreateHabit.setOnClickListener {
            val action = HabitsPagerFragmentDirections.actionGoToHabitFromVP(null)
            findNavController().navigate(action)
        }

        setFragmentResultListener(CreateHabitFragment.SAVE_HABIT_SUCCESS) { _, _ ->
            activity?.let { updateAdapter(it) }
        }

        return binding.root
    }

    private fun updateAdapter(activity: FragmentActivity) {
        binding.habitsPagerViewPager.adapter = PagerAdapter(activity) { habit: Habit ->
            val action = HabitsPagerFragmentDirections.actionGoToHabitFromVP(habit)
            findNavController().navigate(action)
        }
    }
}