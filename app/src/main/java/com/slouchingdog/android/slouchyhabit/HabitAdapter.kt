package com.slouchingdog.android.slouchyhabit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.slouchingdog.android.slouchyhabit.databinding.ItemHabitBinding
import java.util.Locale

class HabitAdapter(
    val habits: List<Habit>,
    val onHabitClicked: (habit: Habit) -> Unit
) : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {
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
        holder.bind(habit, onHabitClicked)
    }

    override fun getItemCount() = habits.size

    class HabitViewHolder(val binding: ItemHabitBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(habit: Habit, onHabitClicked: (habit: Habit) -> Unit) {
            binding.tvHabitItemTitle.text = habit.title
            binding.tvHabitItemDescription.text = habit.description
            binding.tvHabitItemType.text = habit.type.title
            binding.tvHabitItemPriority.text = habit.priority
            val timesCountString = setDeclension(habit.periodicityTimes, "раз", "раза", "раз")
            val daysCountString = setDeclension(habit.periodicityDays, "день", "дня", "дней")
            binding.tvHabitItemPeriodicity.text = String.format(
                Locale.getDefault(),
                "%d %s в %d %s",
                habit.periodicityTimes,
                timesCountString,
                habit.periodicityDays,
                daysCountString
            )
            binding.root.setCardBackgroundColor(habit.color)

            binding.root.setOnClickListener {
                onHabitClicked(habit)
            }
        }
    }
}