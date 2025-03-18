package com.slouchingdog.android.slouchyhabit.ui.habits_pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.slouchingdog.android.slouchyhabit.data.Habit
import com.slouchingdog.android.slouchyhabit.data.HabitType
import com.slouchingdog.android.slouchyhabit.ui.habits_list.HabitsListFragment

class PagerAdapter(
    activity: FragmentActivity,
    val onCardClick: (habit: Habit) -> Unit
) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment =
        if (position == 0) HabitsListFragment.newInstance(HabitType.GOOD, onCardClick)
        else HabitsListFragment.newInstance(HabitType.BAD, onCardClick)

    override fun getItemCount(): Int = 2

    override fun onBindViewHolder(holder: FragmentViewHolder, position: Int, payloads: List<Any?>) {
        super.onBindViewHolder(holder, position, payloads)
        getItemId(position)
    }
}
