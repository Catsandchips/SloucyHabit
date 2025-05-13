package com.slouchingdog.android.slouchyhabit.presentation.ui.habit_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.slouchingdog.android.slouchyhabit.databinding.FragmentFilterBottomSheetBinding
import com.slouchingdog.android.slouchyhabit.di.subcomponents.FilterBottomSheetSubcomponent
import javax.inject.Inject

class FilterBottomSheetFragment() : BottomSheetDialogFragment() {
    lateinit var binding: FragmentFilterBottomSheetBinding
    @Inject
    lateinit var viewModelFactory: HabitsListViewModelFactory
    val viewModel: HabitsListViewModel by activityViewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        FilterBottomSheetSubcomponent.inject(this)
        super.onCreate(savedInstanceState)
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

