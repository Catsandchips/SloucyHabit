package com.slouchingdog.android.slouchyhabit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.slouchingdog.android.slouchyhabit.databinding.FragmentHabitsListBinding

class HabitsListFragment : Fragment() {
    lateinit var binding: FragmentHabitsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHabitsListBinding.inflate(layoutInflater)

        binding.habitRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = HabitAdapter(HabitsStorage.habits) { habit: Habit ->
            val action = HabitsListFragmentDirections.actionGoToHabitFromCard(habit)
            findNavController().navigate(action)
        }
        binding.habitRecyclerView.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabCreateHabit.setOnClickListener{
            val action = HabitsListFragmentDirections.actionGoToHabitFromCard(null)
            findNavController().navigate(action)
        }

    }
}