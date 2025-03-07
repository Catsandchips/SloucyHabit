package com.slouchingdog.android.slouchyhabit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.slouchingdog.android.slouchyhabit.databinding.FragmentCreateHabitBinding
import java.util.Locale

class CreateHabitFragment : Fragment() {

    lateinit var binding: FragmentCreateHabitBinding
    val args: CreateHabitFragmentArgs by navArgs()
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

        val habitArgument = args.habit
        var habitId: Int
        if (habitArgument != null) {
            val habit = habitArgument
            habitId = habit.id
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
        } else {
            habitId = HabitsStorage.habits.size
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
                Snackbar.make(binding.root, "Заполните все поля", Snackbar.LENGTH_LONG).show()
            } else {
                val habitName = binding.etHabitNameField.text.toString()
                val habitDescription = binding.etHabitDescriptionField.text.toString()
                val checkedHabitTypeRadio = binding.rgHabitTypeField.checkedRadioButtonId
                val habitType = when (checkedHabitTypeRadio) {
                    binding.rbGoodHabitRadio.id -> HabitType.GOOD
                    binding.rbBadHabitRadio.id -> HabitType.BAD
                    else -> HabitType.GOOD
                }
                val habitPriority = binding.habitPrioritySpinner.selectedItem.toString()
                val periodicityTimes = binding.etRepetitionsField.text.toString().toInt()
                val periodicityDays = binding.etDaysCountField.text.toString().toInt()

                val newHabit = Habit(
                    habitId,
                    habitName,
                    habitDescription,
                    habitType,
                    habitPriority,
                    periodicityTimes,
                    periodicityDays
                )

                if (habitArgument != null) {
                    HabitsStorage.habits[habitId] = newHabit
                } else
                    HabitsStorage.habits.add(newHabit)

                findNavController().popBackStack()
            }
        }

        return binding.root
    }
}
