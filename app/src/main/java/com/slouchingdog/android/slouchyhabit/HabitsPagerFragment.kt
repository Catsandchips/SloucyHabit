package com.slouchingdog.android.slouchyhabit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.slouchingdog.android.slouchyhabit.databinding.FragmentHabitsPagerBinding

class HabitsPagerFragment : Fragment() {
    lateinit var binding: FragmentHabitsPagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHabitsPagerBinding.inflate(layoutInflater)

        activity?.let { activity ->
            binding.habitsPagerViewPager.adapter = PagerAdapter(activity)
            TabLayoutMediator(
                binding.habitsPagerTabLayout,
                binding.habitsPagerViewPager
            ) { tab, position ->
                tab.text = "ТИП ПРИВЫЧКИ $position"
            }.attach()
        }

        binding.fabCreateHabit.setOnClickListener {
            val action = HabitsPagerFragmentDirections.actionGoToHabitFromVP(null)
            findNavController().navigate(action)
        }

        return binding.root
    }
}