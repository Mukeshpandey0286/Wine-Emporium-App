package com.example.flavorfiesta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.flavorfiesta.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
   private val binding : ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, NextSplashActivity::class.java)
            startActivity(intent)
            finish()
        },1600)

    }
}