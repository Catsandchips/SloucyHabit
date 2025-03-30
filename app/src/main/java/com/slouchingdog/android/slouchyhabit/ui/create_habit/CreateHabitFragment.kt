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
        binding.rbGoodHabitRadio.text = resources.getString(HabitType.GOOD.title)
        binding.rbBadHabitRadio.text = resources.getString(HabitType.BAD.title)
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
        binding.etHabitNameField.addTextChangedListener(afterTextChanged = {
            viewModel.onTitleChange(it.toString())
        })
    }

    private fun onHabitDescriptionChange() {
        binding.etHabitDescriptionField.addTextChangedListener(afterTextChanged = {
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
        binding.rgHabitTypeField.setOnCheckedChangeListener { _, checkedRadioButtonId ->
            val type = when (checkedRadioButtonId) {
                binding.rbGoodHabitRadio.id -> HabitType.GOOD
                binding.rbBadHabitRadio.id -> HabitType.BAD
                else -> HabitType.GOOD
            }
            viewModel.onTypeChange(type)
        }
    }

    private fun onHabitPeriodicityTimesChange() {
        binding.etRepetitionsField.addTextChangedListener(afterTextChanged = { input ->
            val count = if (input.isNullOrEmpty()) 0 else input.toString().toInt()
            binding.tvRepetitionsFieldText.text =
                resources.getQuantityString(R.plurals.times, count)
            viewModel.onPeriodicityTimesChange(count)
        })
    }

    private fun onHabitPeriodicityDaysChange() {
        binding.etDaysCountField.addTextChangedListener(afterTextChanged = { input ->
            val count = if (input.isNullOrEmpty()) 0 else input.toString().toInt()
            binding.tvDaysCountFieldText.text = resources.getQuantityString(R.plurals.days, count)
            viewModel.onPeriodicityDaysChange(count)
        })
    }

    private fun onSaveButtonClick(){
        binding.btnSaveHabit.setOnClickListener {
            if (binding.etHabitNameField.text.isEmpty() || binding.etHabitDescriptionField.text.isEmpty() || binding.etRepetitionsField.text.isEmpty() || binding.etDaysCountField.text.isEmpty()) {
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
        binding.etHabitNameField.setText(viewModel.habitState.title)
        binding.etHabitDescriptionField.setText(viewModel.habitState.description)
        binding.habitPrioritySpinner.setSelection(viewModel.habitState.priority)
        binding.rgHabitTypeField.check(
            when (viewModel.habitState.type) {
                HabitType.GOOD -> binding.rbGoodHabitRadio.id
                HabitType.BAD -> binding.rbBadHabitRadio.id
            }
        )
        binding.etRepetitionsField.setText(viewModel.habitState.periodicityTimes.toString())
        binding.tvRepetitionsFieldText.text =
            resources.getQuantityString(R.plurals.times, viewModel.habitState.periodicityTimes)

        binding.etDaysCountField.setText(viewModel.habitState.periodicityDays.toString())
        binding.tvDaysCountFieldText.text =
            resources.getQuantityString(R.plurals.days, viewModel.habitState.periodicityDays)
    }
}
