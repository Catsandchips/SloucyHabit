package com.slouchingdog.android.slouchyhabit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.slouchingdog.android.slouchyhabit.databinding.FragmentFilterBottomSheetBinding
import com.slouchingdog.android.slouchyhabit.ui.habits_list.HabitsListViewModel

class FilterBottomSheetFragment() : BottomSheetDialogFragment() {
    lateinit var binding: FragmentFilterBottomSheetBinding
    val viewModel: HabitsListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilterBottomSheetBinding.inflate(inflater)

        binding.etTitleFilter.setText(viewModel.titleQuery)

        binding.etTitleFilter.addTextChangedListener(afterTextChanged = { text ->
            viewModel.setFilters(text.toString())
        })

        binding.btnClearFilter.setOnClickListener {
            binding.etTitleFilter.text.clear()
        }

        binding.btnSortByPriorityDesc.setOnClickListener {
            viewModel.setPrioritySorting(sortAsc = false)
        }

        binding.btnSortByPriorityAsc.setOnClickListener {
            viewModel.setPrioritySorting(sortAsc = true)
        }

        return binding.root
    }
}

