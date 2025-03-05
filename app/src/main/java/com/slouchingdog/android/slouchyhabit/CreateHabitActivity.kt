package com.slouchingdog.android.slouchyhabit

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import com.slouchingdog.android.slouchyhabit.databinding.ActivityCreateHabitBinding
import java.util.Locale

class CreateHabitActivity : AppCompatActivity() {
    lateinit var binding: ActivityCreateHabitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateHabitBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onStart() {
        super.onStart()

        binding.rbGoodHabitRadio.text = HabitType.GOOD.title
        binding.rbBadHabitRadio.text = HabitType.BAD.title

        val habitExtra = intent.getSerializableExtra(CURRENT_HABIT)
        var habitId: Int
        if (habitExtra != null) {
            val habit = habitExtra as Habit
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
                setDeclension(habit.periodicityTimes, "раз в", "раза в", "раз в")
            binding.etDaysCountField.setText(
                String.format(
                    Locale.getDefault(),
                    "%d",
                    habit.periodicityDays
                )
            )
            binding.tvDaysCountFieldText.text =
                setDeclension(habit.periodicityDays, "день", "дня", "дней")
        } else {
            habitId = HabitsStorage.habits.size
        }

        binding.etRepetitionsField.addTextChangedListener(afterTextChanged = { p0 ->
            binding.tvRepetitionsFieldText.text =
                if (!p0.isNullOrEmpty()) {
                    setDeclension(p0.toString().toInt(), "раз в", "раза в", "раз в")
                } else {
                    "раз в"
                }
        })

        binding.etDaysCountField.addTextChangedListener(afterTextChanged = { p0 ->
            binding.tvDaysCountFieldText.text =
                if (!p0.isNullOrEmpty()) {
                    setDeclension(p0.toString().toInt(), "день", "дня", "дней")
                } else {
                    "дней"
                }
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

                if (habitExtra != null) {
                    HabitsStorage.habits[habitId] = newHabit
                } else
                    HabitsStorage.habits.add(newHabit)

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

}