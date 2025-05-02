package com.slouchingdog.android.slouchyhabit.ui.habits_pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.slouchingdog.android.slouchyhabit.R
import com.slouchingdog.android.slouchyhabit.databinding.FragmentHabitsPagerBinding
import com.slouchingdog.android.slouchyhabit.ui.habits_list.FilterBottomSheetFragment

class HabitsPagerFragment : Fragment() {
    lateinit var binding: FragmentHabitsPagerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHabitsPagerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            binding.habitsPagerViewPager.adapter = PagerAdapter(it)
            TabLayoutMediator(
                binding.habitsPagerTabLayout,
                binding.habitsPagerViewPager,
                true
            ) { tab, position ->
                when (position) {
                    0 -> tab.text = resources.getString(R.string.good_habits_tab_title)
                    1 -> tab.text = resources.getString(R.string.bad_habits_tab_title)
                }
            }.attach()
        }

        val bottomSheet = FilterBottomSheetFragment()

        binding.openFilterButton.setOnClickListener {
            bottomSheet.show(parentFragmentManager, "FilterBottomSheet")
        }

        binding.createHabitButton.setOnClickListener {
            val action = HabitsPagerFragmentDirections.actionGoToHabitFromVP()
            findNavController().navigate(action)
        }
    }
}