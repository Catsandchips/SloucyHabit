package com.slouchingdog.android.slouchyhabit.ui.habits_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.slouchingdog.android.slouchyhabit.SlouchyHabitApplication
import com.slouchingdog.android.slouchyhabit.databinding.FragmentFilterBottomSheetBinding
import com.slouchingdog.android.slouchyhabit.di.AppComponent

class FilterBottomSheetFragment() : BottomSheetDialogFragment() {
    lateinit var binding: FragmentFilterBottomSheetBinding
    val appComponent: AppComponent by lazy {
        (requireActivity().application as SlouchyHabitApplication).appComponent
    }
    val viewModel: HabitsListViewModel by activityViewModels {
        HabitsListViewModelFactory(
            appComponent.getGetHabitsUseCase(),
            appComponent.getDeleteHabitUseCase()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilterBottomSheetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.titleFilterField.setText(viewModel.titleQuery)

        binding.titleFilterField.addTextChangedListener(afterTextChanged = { text ->
            viewModel.filterHabits(text.toString())
        })

        binding.sortByPriorityDescButton.setOnClickListener {
            if (binding.sortByPriorityDescButton.isChecked) {
                binding.sortByPriorityAscButton.isChecked = false
                viewModel.setPrioritySorting(sortByAsc = false)
            } else {
                viewModel.resetPrioritySorting()
            }
        }

        binding.sortByPriorityAscButton.setOnClickListener {
            if (binding.sortByPriorityAscButton.isChecked) {
                binding.sortByPriorityDescButton.isChecked = false
                viewModel.setPrioritySorting(sortByAsc = true)

            } else {
                viewModel.resetPrioritySorting()
            }
        }
    }
}

