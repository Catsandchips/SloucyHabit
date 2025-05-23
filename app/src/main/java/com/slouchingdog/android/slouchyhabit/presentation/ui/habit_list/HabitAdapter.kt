package com.slouchingdog.android.slouchyhabit.presentation.ui.habit_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.slouchingdog.android.domain.entity.HabitEntity
import com.slouchingdog.android.slouchyhabit.R
import com.slouchingdog.android.slouchyhabit.databinding.ItemHabitBinding
import com.slouchingdog.android.slouchyhabit.presentation.ui.create_habit.HABIT_ID_ARG
import java.util.Locale

class HabitAdapter(
    val onDeleteButtonClick: (habit: HabitEntity) -> Unit,
    val onDoneButtonClick: (habitEntity: HabitEntity) -> Unit
) :
    RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {
    private var habits: List<HabitEntity> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HabitViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHabitBinding.inflate(inflater, parent, false)
        return HabitViewHolder(binding, onDeleteButtonClick, onDoneButtonClick)
    }

    override fun onBindViewHolder(
        holder: HabitViewHolder,
        position: Int
    ) {
        val habit = habits[position]
        holder.bind(habit)
    }

    override fun getItemCount() = habits.size

    fun updateHabits(newHabits: List<HabitEntity>) {
        val diffResult = DiffUtil.calculateDiff(HabitDiffCallback(habits, newHabits))
        habits = newHabits
        diffResult.dispatchUpdatesTo(this)
    }

    class HabitViewHolder(
        val binding: ItemHabitBinding,
        val onDeleteButtonClick: (habitEntity: HabitEntity) -> Unit,
        val onDoneButtonClick: (habitEntity: HabitEntity) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(habit: HabitEntity) {
            binding.habitItemTitle.text = habit.title
            binding.habitItemDescription.text = habit.description
            binding.habitItemType.text = habit.type.title
            binding.habitItemPriority.text =
                itemView.resources.getStringArray(R.array.priorities_array)[habit.priority]
            val timesCountString =
                itemView.resources.getQuantityString(R.plurals.times_in, habit.periodicityTimes)
            val daysCountString =
                itemView.resources.getQuantityString(R.plurals.days, habit.periodicityDays)
            binding.habitItemPeriodicity.text = String.format(
                Locale.getDefault(),
                "%d %s %d %s",
                habit.periodicityTimes,
                timesCountString,
                habit.periodicityDays,
                daysCountString
            )

            binding.root.setOnClickListener {
                val bundle = bundleOf(HABIT_ID_ARG to habit.id)
                itemView.findNavController().navigate(R.id.nav_create, bundle)
            }

            binding.deleteHabitButton.setOnClickListener {
                onDeleteButtonClick(habit)
            }

            binding.habitDoneButton.setOnClickListener {
                onDoneButtonClick(habit)
            }
        }
    }
}