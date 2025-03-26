package com.slouchingdog.android.slouchyhabit.ui.habits_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.slouchingdog.android.slouchyhabit.databinding.FragmentFilterBottomSheetBinding

class FilterBottomSheetFragment() : BottomSheetDialogFragment() {
    lateinit var binding: FragmentFilterBottomSheetBinding
    val viewModel: HabitsListViewModel by activityViewModels()

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

        binding.etTitleFilter.setText(viewModel.titleQuery)
        binding.btnClearFilter.isVisible = false

        binding.etTitleFilter.addTextChangedListener(afterTextChanged = { text ->
            if (!text.isNullOrEmpty()) {
                binding.btnClearFilter.isVisible = true
            } else {
                binding.btnClearFilter.isVisible = false
            }
            viewModel.filterHabits(text.toString())
        })

        binding.btnClearFilter.setOnClickListener {
            binding.etTitleFilter.text.clear()
        }

        binding.btnSortByPriorityDesc.setOnClickListener {
            if (binding.btnSortByPriorityDesc.isChecked) {
                binding.btnSortByPriorityAsc.isChecked = false
                viewModel.setPrioritySorting(sortByAsc = false)
            } else {
                viewModel.resetPrioritySorting()
            }
        }

        binding.btnSortByPriorityAsc.setOnClickListener {
            if (binding.btnSortByPriorityAsc.isChecked) {
                binding.btnSortByPriorityDesc.isChecked = false
                viewModel.setPrioritySorting(sortByAsc = true)

            } else {
                viewModel.resetPrioritySorting()
            }
        }
    }
}

