package com.slouchingdog.android.slouchyhabit.ui.habits_list

import androidx.recyclerview.widget.DiffUtil
import com.slouchingdog.android.slouchyhabit.data.entity.HabitDBO

class HabitDiffCallback(
    private val oldList: List<HabitDBO>,
    private val newList: List<HabitDBO>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}