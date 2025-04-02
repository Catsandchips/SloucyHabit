package com.slouchingdog.android.slouchyhabit.ui.create_habit

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.slouchingdog.android.slouchyhabit.R
import com.slouchingdog.android.slouchyhabit.data.HabitType
import com.slouchingdog.android.slouchyhabit.databinding.FragmentCreateHabitBinding

const val HABIT_ID_ARG = "HABIT_ARG"

class CreateHabitFragment : Fragment() {

    lateinit var binding: FragmentCreateHabitBinding
    val viewModel: CreateHabitViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateHabitBinding.inflate(inflater)
        binding.goodHabitRadioButton.text = resources.getString(HabitType.GOOD.title)
        binding.badHabitRadioButton.text = resources.getString(HabitType.BAD.title)
        setCurrentFormState()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onHabitTitleChange()
        onHabitDescriptionChange()
        onHabitPriorityChange()
        onHabitTypeChange()
        onHabitPeriodicityTimesChange()
        onHabitPeriodicityDaysChange()
        onSaveButtonClick()
    }

    private fun onHabitTitleChange() {
        binding.habitNameField.addTextChangedListener(afterTextChanged = {
            viewModel.onTitleChange(it.toString())
        })
    }

    private fun onHabitDescriptionChange() {
        binding.habitDescriptionField.addTextChangedListener(afterTextChanged = {
            viewModel.onDescriptionChange(it.toString())
        })
    }

    private fun onHabitPriorityChange() {
        binding.habitPrioritySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.onPriorityChange(position)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
    }

    private fun onHabitTypeChange() {
        binding.habitTypeRadioGroup.setOnCheckedChangeListener { _, checkedRadioButtonId ->
            val type = when (checkedRadioButtonId) {
                binding.goodHabitRadioButton.id -> HabitType.GOOD
                binding.badHabitRadioButton.id -> HabitType.BAD
                else -> HabitType.GOOD
            }
            viewModel.onTypeChange(type)
        }
    }

    private fun onHabitPeriodicityTimesChange() {
        binding.repetitionsField.addTextChangedListener(afterTextChanged = { input ->
            val count = if (input.isNullOrEmpty()) 0 else input.toString().toInt()
            binding.repetitionsFieldText.text =
                resources.getQuantityString(R.plurals.times, count)
            viewModel.onPeriodicityTimesChange(count)
        })
    }

    private fun onHabitPeriodicityDaysChange() {
        binding.daysCountField.addTextChangedListener(afterTextChanged = { input ->
            val count = if (input.isNullOrEmpty()) 0 else input.toString().toInt()
            binding.daysCountFieldText.text = resources.getQuantityString(R.plurals.days, count)
            viewModel.onPeriodicityDaysChange(count)
        })
    }

    private fun onSaveButtonClick() {
        binding.saveHabitButton.setOnClickListener {
            if (binding.habitNameField.text.isEmpty() || binding.habitDescriptionField.text.isEmpty() || binding.repetitionsField.text.isEmpty() || binding.daysCountField.text.isEmpty()) {
                Snackbar.make(
                    binding.root,
                    resources.getString(R.string.form_validation_text),
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                viewModel.addHabit()

                findNavController().navigateUp()
            }
        }
    }

    private fun setCurrentFormState() {
        val passedHabitId = arguments?.getInt(HABIT_ID_ARG, -1)
        if (passedHabitId != null && passedHabitId != -1) {
            viewModel.setHabitState(passedHabitId)
            prefillFormWithState()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun prefillFormWithState() {
        (activity as AppCompatActivity).supportActionBar?.title =
            resources.getString(R.string.edit_habit_title)
        binding.habitNameField.setText(viewModel.habitState.title)
        binding.habitDescriptionField.setText(viewModel.habitState.description)
        binding.habitPrioritySpinner.setSelection(viewModel.habitState.priority)
        binding.habitTypeRadioGroup.check(
            when (viewModel.habitState.type) {
                HabitType.GOOD -> binding.goodHabitRadioButton.id
                HabitType.BAD -> binding.badHabitRadioButton.id
            }
        )
        binding.repetitionsField.setText(viewModel.habitState.periodicityTimes.toString())
        binding.repetitionsFieldText.text =
            resources.getQuantityString(R.plurals.times, viewModel.habitState.periodicityTimes)

        binding.daysCountField.setText(viewModel.habitState.periodicityDays.toString())
        binding.daysCountFieldText.text =
            resources.getQuantityString(R.plurals.days, viewModel.habitState.periodicityDays)
    }
}
