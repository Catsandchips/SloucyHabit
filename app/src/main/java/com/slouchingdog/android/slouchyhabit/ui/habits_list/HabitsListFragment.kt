package com.slouchingdog.android.slouchyhabit.ui.habits_list

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.slouchingdog.android.slouchyhabit.data.HabitType
import com.slouchingdog.android.slouchyhabit.data.HabitsStorage
import com.slouchingdog.android.slouchyhabit.databinding.FragmentHabitsListBinding

private const val HABIT_TYPE_PARAM = "HABIT_TYPE_PARAM"

class HabitsListFragment() : Fragment() {
    lateinit var binding: FragmentHabitsListBinding
    private var habitsType: HabitType? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            habitsType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable(HABIT_TYPE_PARAM, HabitType::class.java)
            } else {
                it.getSerializable(HABIT_TYPE_PARAM) as HabitType?
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHabitsListBinding.inflate(layoutInflater)

        binding.habitRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = HabitAdapter(HabitsStorage.getHabitsWithType(habitsType))
        binding.habitRecyclerView.adapter = adapter

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(habitsType: HabitType): HabitsListFragment =
            HabitsListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(HABIT_TYPE_PARAM, habitsType)
                }
            }
    }
}