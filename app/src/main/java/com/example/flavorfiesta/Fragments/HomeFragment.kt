package com.example.flavorfiesta.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.flavorfiesta.Adapter.MenuAdapter
import com.example.flavorfiesta.Models.MenuItem
import com.example.flavorfiesta.R
import com.example.flavorfiesta.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var database : FirebaseDatabase
    private lateinit var menusItems : MutableList<MenuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Set up LottieAnimationView
        val loadingAnimationView = binding.loadingAnimationView
        loadingAnimationView.setAnimation(R.raw.loading) // Replace with your animation file
        loadingAnimationView.playAnimation()

        binding.viewMenu.setOnClickListener {
            val bottomSheetDialogue = MenuBottomSheetFragment()
            bottomSheetDialogue.show(parentFragmentManager, "test")
        }

        retrievePopularData()
        return binding.root

    }

    private fun retrievePopularData() {
        binding.loadingAnimationView.visibility = View.VISIBLE // Show Lottie animation

//        refrece to the database
        database= FirebaseDatabase.getInstance()
        val foodRef : DatabaseReference = database.reference.child("menu")
        menusItems = mutableListOf()

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children){
                    val menuItem = foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let { menusItems.add(it) }
                }
//                display ramdom popular items
                randomPopularData()
                binding.loadingAnimationView.visibility = View.GONE // Hide Lottie animation
            }

            private fun randomPopularData() {
                val index = menusItems.indices.toList().shuffled()
                val maxItemShow = 6
                val subsetMenuItem = index.take(maxItemShow).map { menusItems[it] }

                setPopularItems(subsetMenuItem)
            }

            private fun setPopularItems(subsetMenuItem: List<MenuItem>) {
                val adapter = MenuAdapter(subsetMenuItem,requireContext())
                binding.popularRecyclerview.layoutManager = LinearLayoutManager(requireContext())
                binding.popularRecyclerview.adapter = adapter

                // Notify the adapter when the data set changes
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                binding.loadingAnimationView.visibility = View.GONE // Hide Lottie animation
            }
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.wine1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.wine2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.wine3, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.wine4, ScaleTypes.FIT))
        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.FIT)
        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun doubleClick(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(position: Int) {
                val itemPos = imageList[position]
                val itemMessage = "Selected Image $position"
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show()
            }
        })

    }
}