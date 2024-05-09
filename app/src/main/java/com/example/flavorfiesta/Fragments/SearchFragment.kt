package com.example.flavorfiesta.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flavorfiesta.Adapter.MenuAdapter
import com.example.flavorfiesta.Models.MenuItem
import com.example.flavorfiesta.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    private lateinit var binding : FragmentSearchBinding
    private lateinit var adapter: MenuAdapter

    private var originalMenuList: List<MenuItem> = emptyList() // Initialize as an empty list

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        // Save the original list for filtering
//        originalMenuList = menuProducts.toList()

         adapter = MenuAdapter(originalMenuList.toMutableList(), requireContext())

        binding.menuRcv.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRcv.adapter = adapter

//        setup for search view..

        setupSearchView()
        return binding.root
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Save the original list for filtering
                adapter.filter(newText.orEmpty())
                return true
            }
        })

    }

//    private fun filterMenuItems(query: String?) {
//        // Filter the original list based on the query
//        val filteredList = originalMenuList.filter {
//            it.menuDishName.contains(query.orEmpty(), ignoreCase = true)
//        }

        // Update the adapter with the filtered data
//        adapter.updateList(filteredList)
//    }

    companion object {

    }
}