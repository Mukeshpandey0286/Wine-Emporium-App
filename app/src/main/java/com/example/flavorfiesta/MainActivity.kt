package com.example.flavorfiesta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.flavorfiesta.Fragments.NotificationBottomViewFragment
import com.example.flavorfiesta.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setting up the bottomNavigation with the help of findNavController....
        //and by this which is selected in bottom is reflect on the fragment or main display

        var navController = findNavController(R.id.fragmentContainerView)
        var bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setupWithNavController(navController)

//        for notification icon bottom sheet show

        binding.notificationIcon.setOnClickListener {
            val bottomSheetDialogue = NotificationBottomViewFragment()
            bottomSheetDialogue.show(supportFragmentManager, "Test")
        }
    }
}