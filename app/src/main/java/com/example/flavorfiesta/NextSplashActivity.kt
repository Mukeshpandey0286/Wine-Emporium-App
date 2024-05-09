package com.example.flavorfiesta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.flavorfiesta.databinding.ActivityNextSplashBinding

class NextSplashActivity : AppCompatActivity() {
    private val binding : ActivityNextSplashBinding by lazy {
        ActivityNextSplashBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.nextBtn.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}