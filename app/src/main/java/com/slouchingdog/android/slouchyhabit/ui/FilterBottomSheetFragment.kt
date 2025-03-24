package com.slouchingdog.android.slouchyhabit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.slouchingdog.android.slouchyhabit.databinding.FragmentFilterBinding
import com.slouchingdog.android.slouchyhabit.ui.habits_list.HabitFilters
import com.slouchingdog.android.slouchyhabit.ui.habits_list.HabitsListViewModel

class FilterBottomSheetFragment(
    private val onFiltersApplied: (HabitFilters) -> Unit
) : BottomSheetDialogFragment() {
    lateinit var binding: FragmentFilterBinding
    val viewModel: HabitsListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilterBinding.inflate(inflater)

        binding.btnSearch.setOnClickListener {
            val filters = HabitFilters(
//                nameQuery = binding.nameEditText.text.toString().takeIf { it.isNotBlank() },
//                type = binding.typeSpinner.selectedItem?.toString()?.takeIf { it.isNotBlank() },
                priority = binding.habitPriorityFilter.selectedItem?.toString()
                    ?.takeIf { it.isNotBlank() }
            )
            onFiltersApplied(filters)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


    }
}

