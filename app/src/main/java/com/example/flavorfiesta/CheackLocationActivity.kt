package com.example.flavorfiesta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.flavorfiesta.databinding.ActivityCheackLocationBinding

class CheackLocationActivity : AppCompatActivity() {
    private val binding : ActivityCheackLocationBinding by lazy {
        ActivityCheackLocationBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val locationList  = arrayOf("Bageshwar", "Haldwani", "Almora", "Pithoragarh")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)
        val autoCompleteTextView = binding.listofloc
        autoCompleteTextView.setAdapter(adapter)
        binding.button.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}