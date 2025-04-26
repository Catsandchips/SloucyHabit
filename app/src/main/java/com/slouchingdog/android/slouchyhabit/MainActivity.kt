package com.slouchingdog.android.slouchyhabit

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.slouchingdog.android.slouchyhabit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        setupNavigationView()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_about
            ), binding.navigationDrawerLayout

        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }

    fun setupNavigationView() {
        val headerView = binding.navView.getHeaderView(0)
        val imgHeader = headerView.findViewById<ImageView>(R.id.user_pic)

        Glide.with(this)
            .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQA8Kti_6N_r_53feMNX-wiVvp9ujv7NXsCvw&s")
            .circleCrop()
            .error(R.mipmap.user_pic_placeholder)
            .placeholder(R.mipmap.user_pic_placeholder)
            .override(250, 250)
            .into(imgHeader)

        binding.navView.setItemBackgroundResource(R.drawable.nav_item_background)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}