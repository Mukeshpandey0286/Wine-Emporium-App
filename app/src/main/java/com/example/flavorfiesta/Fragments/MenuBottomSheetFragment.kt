package com.example.flavorfiesta.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flavorfiesta.Adapter.MenuAdapter
import com.example.flavorfiesta.Models.MenuItem
import com.example.flavorfiesta.R
import com.example.flavorfiesta.databinding.FragmentMenuBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MenuBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentMenuBottomSheetBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems : MutableList<MenuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMenuBottomSheetBinding.inflate(inflater, container, false)

        // Set up LottieAnimationView
        val loadingAnimationView = binding.loadingAnimationView
        loadingAnimationView.setAnimation(R.raw.loading) // Replace with your animation file
        loadingAnimationView.playAnimation()

        binding.arrowBackBtn.setOnClickListener{
            dismiss()
        }

        retrieveData()
        return binding.root
    }


    private fun retrieveData() {
        binding.loadingAnimationView.visibility = View.VISIBLE // Show Lottie animation

        database = FirebaseDatabase.getInstance()
        val foodRef : DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children){
                    val menuItem = foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }
                }
                // Setup adapter and RecyclerView after retrieving data
                val adapter = MenuAdapter(menuItems, requireContext())
                binding.menuRcv.layoutManager = LinearLayoutManager(requireContext())
                binding.menuRcv.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    companion object {

    }
}