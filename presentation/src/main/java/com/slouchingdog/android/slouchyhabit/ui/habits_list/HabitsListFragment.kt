package com.slouchingdog.android.slouchyhabit.ui.habits_list

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.slouchingdog.android.common2.HabitType
import com.slouchingdog.android.data2.entity.HabitDBO
import com.slouchingdog.android.slouchyhabit.SlouchyHabitApplication
import com.slouchingdog.android.slouchyhabit.databinding.FragmentHabitsListBinding
import kotlinx.coroutines.launch

private const val HABIT_TYPE_PARAM = "HABIT_TYPE_PARAM"

class HabitsListFragment() : Fragment() {
    lateinit var binding: FragmentHabitsListBinding
    lateinit var viewModel: HabitsListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = (requireActivity().application as SlouchyHabitApplication).appComponent
        val getHabitByIdUseCase = appComponent.getGetHabitsUseCase()

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HabitsListViewModel(getHabitByIdUseCase) as T
            }
        })[HabitsListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHabitsListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var habitTypeArgument: HabitType? = null
        arguments?.let {
            habitTypeArgument = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable(HABIT_TYPE_PARAM, HabitType::class.java)
            } else {
                it.getSerializable(HABIT_TYPE_PARAM) as HabitType?
            }
        }

        binding.habitRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = HabitAdapter()
        binding.habitRecyclerView.adapter = adapter

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getHabitsFlow(habitTypeArgument)
                    .collect { habits: List<HabitDBO> ->
                        adapter.updateHabits(habits)
                    }
            }
        }
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