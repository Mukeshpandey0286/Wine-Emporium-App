package com.example.flavorfiesta.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flavorfiesta.MainActivity
import com.example.flavorfiesta.R
import com.example.flavorfiesta.databinding.FragmentCongratulationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CongratulationFragment : BottomSheetDialogFragment() {
   private lateinit var binding : FragmentCongratulationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCongratulationBinding.inflate(inflater,container,false)

        binding.gohome.setOnClickListener {
            val intent  = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    companion object {

    }
}