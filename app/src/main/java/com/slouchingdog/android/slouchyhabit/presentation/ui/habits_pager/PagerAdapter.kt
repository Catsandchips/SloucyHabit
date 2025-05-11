package com.slouchingdog.android.slouchyhabit.presentation.ui.habits_pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.slouchingdog.android.domain.entity.HabitType
import com.slouchingdog.android.slouchyhabit.presentation.ui.habits_list.HabitsListFragment

class PagerAdapter(
    activity: FragmentActivity
) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> HabitsListFragment.Companion.newInstance(HabitType.GOOD)
            1 -> HabitsListFragment.Companion.newInstance(HabitType.BAD)
            else -> throw IllegalArgumentException("Illegal position")
        }

    override fun getItemCount(): Int = 2
}
