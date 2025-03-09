package com.slouchingdog.android.slouchyhabit

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(
    activity: FragmentActivity
) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment =
        if (position == 0) HabitsListFragment.newInstance(HabitType.GOOD)
        else HabitsListFragment.newInstance(HabitType.BAD)

    override fun getItemCount(): Int = 2
}
