package com.slouchingdog.android.slouchyhabit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.slouchingdog.android.slouchyhabit.databinding.FragmentHabitsListBinding

class HabitsListFragment() : Fragment() {
    private var habitsType: HabitType? = null
    private var onCardClick: (habit: Habit) -> Unit = { }
    lateinit var binding: FragmentHabitsListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHabitsListBinding.inflate(layoutInflater)

        binding.habitRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = HabitAdapter(HabitsStorage.getHabitsWithType(habitsType), onCardClick)
        binding.habitRecyclerView.adapter = adapter
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(habitsType: HabitType, onCardClick: (Habit) -> Unit): HabitsListFragment =
            HabitsListFragment().apply {
                this.habitsType = habitsType
                this.onCardClick = onCardClick
            }
    }
}