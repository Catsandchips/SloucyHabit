package com.slouchingdog.android.slouchyhabit.ui.habits_list

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.slouchingdog.android.slouchyhabit.data.Habit
import com.slouchingdog.android.slouchyhabit.data.HabitType
import com.slouchingdog.android.slouchyhabit.data.HabitsStorage
import com.slouchingdog.android.slouchyhabit.databinding.FragmentHabitsListBinding
import java.io.Serializable

private const val HABIT_TYPE_PARAM = "HABIT_TYPE_PARAM"
private const val CARD_CLICK_PARAM = "CARD_CLICK_PARAM"

class HabitsListFragment() : Fragment() {
    lateinit var binding: FragmentHabitsListBinding
    private var habitsType: HabitType? = null
    private var onCardClick: (habit: Habit) -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            habitsType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable(HABIT_TYPE_PARAM, HabitType::class.java)
            } else {
                it.getSerializable(HABIT_TYPE_PARAM) as HabitType?
            }
            onCardClick = it.getSerializable(CARD_CLICK_PARAM) as ((habit: Habit) -> Unit)
        }
    }

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
                arguments = Bundle().apply {
                    putSerializable(HABIT_TYPE_PARAM, habitsType)
                    putSerializable(CARD_CLICK_PARAM, onCardClick as Serializable)
                }
            }
    }
}