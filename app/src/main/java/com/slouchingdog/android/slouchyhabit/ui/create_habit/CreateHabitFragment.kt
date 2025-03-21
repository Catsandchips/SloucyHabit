package com.slouchingdog.android.slouchyhabit.ui.create_habit

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.slouchingdog.android.slouchyhabit.R
import com.slouchingdog.android.slouchyhabit.data.Habit
import com.slouchingdog.android.slouchyhabit.data.HabitType
import com.slouchingdog.android.slouchyhabit.databinding.FragmentCreateHabitBinding
import java.util.Locale

class CreateHabitFragment : Fragment() {

    lateinit var binding: FragmentCreateHabitBinding
    val viewModel: CreateHabitViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateHabitBinding.inflate(layoutInflater)

        binding.rbGoodHabitRadio.text = resources.getString(HabitType.GOOD.title)
        binding.rbBadHabitRadio.text = resources.getString(HabitType.BAD.title)

        val habitArgument = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable("HABIT", Habit::class.java)
        } else {
            arguments?.getSerializable("HABIT") as Habit
        }
        if (habitArgument != null) {
            (activity as AppCompatActivity).supportActionBar?.title =
                resources.getString(R.string.edit_habit_title)
            val habit = habitArgument
            binding.etHabitNameField.setText(habit.title)
            binding.etHabitDescriptionField.setText(habit.description)
            val spinnerTypes = resources.getStringArray(R.array.types_array)
            binding.habitPrioritySpinner.setSelection(spinnerTypes.indexOf(habit.priority))
            binding.rgHabitTypeField.check(
                when (habit.type) {
                    HabitType.GOOD -> binding.rbGoodHabitRadio.id
                    HabitType.BAD -> binding.rbBadHabitRadio.id
                }
            )
            binding.etRepetitionsField.setText(
                String.format(
                    Locale.getDefault(),
                    "%d",
                    habit.periodicityTimes
                )
            )
            binding.tvRepetitionsFieldText.text =
                resources.getQuantityString(R.plurals.times, habit.periodicityTimes)
            binding.etDaysCountField.setText(
                String.format(
                    Locale.getDefault(),
                    "%d",
                    habit.periodicityDays
                )
            )
            binding.tvDaysCountFieldText.text =
                resources.getQuantityString(R.plurals.days, habit.periodicityDays)
        }

        binding.etRepetitionsField.addTextChangedListener(afterTextChanged = { p0 ->
            val count = if (p0.isNullOrEmpty()) 0 else p0.toString().toInt()
            binding.tvRepetitionsFieldText.text =
                resources.getQuantityString(R.plurals.times, count)

        })

        binding.etDaysCountField.addTextChangedListener(afterTextChanged = { p0 ->
            val count = if (p0.isNullOrEmpty()) 0 else p0.toString().toInt()
            binding.tvDaysCountFieldText.text =
                resources.getQuantityString(R.plurals.days, count)
        })


        binding.btnSaveHabit.setOnClickListener {
            if (binding.etHabitNameField.text.isEmpty() || binding.etHabitDescriptionField.text.isEmpty() || binding.etRepetitionsField.text.isEmpty() || binding.etDaysCountField.text.isEmpty()) {
                Snackbar.make(
                    binding.root,
                    resources.getString(R.string.form_validation_text),
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                viewModel.addHabit(
                    id = habitArgument?.id,
                    title = binding.etHabitNameField.text.toString(),
                    description = binding.etHabitDescriptionField.text.toString(),
                    type = when (binding.rgHabitTypeField.checkedRadioButtonId) {
                        binding.rbGoodHabitRadio.id -> HabitType.GOOD
                        binding.rbBadHabitRadio.id -> HabitType.BAD
                        else -> HabitType.GOOD
                    },
                    priority = binding.habitPrioritySpinner.selectedItem.toString(),
                    periodicityTimes = binding.etRepetitionsField.text.toString().toInt(),
                    periodicityDays = binding.etDaysCountField.text.toString().toInt()

                )

                setFragmentResult(SAVE_HABIT_SUCCESS, Bundle())
                findNavController().navigateUp()
            }
        }

        return binding.root
    }

    companion object {
        const val SAVE_HABIT_SUCCESS = "SAVE_HABIT_SUCCESS"
    }
}
