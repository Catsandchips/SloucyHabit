package com.slouchingdog.android.slouchyhabit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.slouchingdog.android.slouchyhabit.databinding.ActivityMainBinding

const val CURRENT_HABIT = "CURRENT HABIT"

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.habitRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = HabitAdapter(HabitsStorage.habits) { habit: Habit ->
            val intent = Intent(this, CreateHabitActivity::class.java)
            intent.putExtra(CURRENT_HABIT, habit)
            startActivity(intent)
        }
        binding.habitRecyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()

        binding.fabCreateHabit.setOnClickListener {
            val intent = Intent(this, CreateHabitActivity::class.java)
            startActivity(intent)
        }

    }
}