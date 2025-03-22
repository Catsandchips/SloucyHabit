package com.slouchingdog.android.slouchyhabit.ui.habits_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.slouchingdog.android.slouchyhabit.R
import com.slouchingdog.android.slouchyhabit.data.Habit
import com.slouchingdog.android.slouchyhabit.databinding.ItemHabitBinding
import com.slouchingdog.android.slouchyhabit.ui.create_habit.HABIT_ARG
import java.util.Locale

class HabitAdapter() : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {
    private var habits: List<Habit> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HabitViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHabitBinding.inflate(inflater, parent, false)
        return HabitViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HabitViewHolder,
        position: Int
    ) {
        val habit = habits[position]
        holder.bind(habit)
    }

    override fun getItemCount() = habits.size

    fun updateHabits(newHabits: List<Habit>) {
        val diffResult = DiffUtil.calculateDiff(HabitDiffCallback(habits, newHabits))
        habits = newHabits
        diffResult.dispatchUpdatesTo(this)
    }

    class HabitViewHolder(val binding: ItemHabitBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(habit: Habit) {
            binding.tvHabitItemTitle.text = habit.title
            binding.tvHabitItemDescription.text = habit.description
            binding.tvHabitItemType.text = itemView.resources.getString(habit.type.title)
            binding.tvHabitItemPriority.text = habit.priority
            val timesCountString =
                itemView.resources.getQuantityString(R.plurals.times, habit.periodicityTimes)
            val daysCountString =
                itemView.resources.getQuantityString(R.plurals.days, habit.periodicityDays)
            binding.tvHabitItemPeriodicity.text = String.format(
                Locale.getDefault(),
                "%d %s %d %s",
                habit.periodicityTimes,
                timesCountString,
                habit.periodicityDays,
                daysCountString
            )
            binding.root.setCardBackgroundColor(habit.color)

            binding.root.setOnClickListener {
                val bundle = bundleOf(HABIT_ARG to habit)
                itemView.findNavController().navigate(R.id.nav_create, bundle)
            }
        }
    }
}