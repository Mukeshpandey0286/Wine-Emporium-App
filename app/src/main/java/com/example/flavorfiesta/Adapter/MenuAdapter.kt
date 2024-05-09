package com.example.flavorfiesta.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flavorfiesta.DetailsActivity
import com.example.flavorfiesta.Models.MenuItem
import com.example.flavorfiesta.databinding.MenuItemBinding
import kotlinx.coroutines.launch

//data class MenuItems(val menuDishName: String, val menuDishPrice: String, val menuDishImg: Int)

class MenuAdapter(private val menuProducts: List<MenuItem>, private val requireContext: Context) :
    RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    private var filteredMenuList: MutableList<MenuItem> = menuProducts.toMutableList()
//    private var itemClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return filteredMenuList.size
    }

    inner class MenuViewHolder(private val binding: MenuItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                openDetailActivity(position)
            }
        }

        fun bind(position: Int) {
            val totalMenu = filteredMenuList[position]
            binding.apply {
                menuFoodName.text = totalMenu.foodName
                menuFoodAmmount.text = totalMenu.foodPrice
                restroNam.text = totalMenu.restroName
                // Check if menuDishImg is not null before loading the image
                totalMenu.foodImage?.let { imageUrl ->
                    val uri = Uri.parse(imageUrl)
                    Glide.with(requireContext).load(uri).into(menuFoodImg)
                }
            }
        }
    }

    private fun openDetailActivity(position: Int) {
    val menuItemm = menuProducts[position]
        val intent = Intent(requireContext, DetailsActivity::class.java).apply {
            putExtra("menuItemName", menuItemm.foodName)
            putExtra("menuRestroName", menuItemm.restroName)
            putExtra("menuItemImage", menuItemm.foodImage)
            putExtra("menuItemPrice", menuItemm.foodPrice)
            putExtra("menuItemDescription", menuItemm.foodDescription)
            putExtra("menuDishIngrediants", menuItemm.foodIngrediant)
        }
        requireContext.startActivity(intent)
    }

    private fun updateList(newList: List<MenuItem>) {
        filteredMenuList.clear()
        filteredMenuList.addAll(newList)
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val newList = if (query.isEmpty()) {
                menuProducts.toList()
            } else {
                performFilterInBackground(query)
            }
            updateList(newList)
        }
    }

    private suspend fun performFilterInBackground(query: String): List<MenuItem> =
        withContext(Dispatchers.Default) {
            menuProducts.filter { menuItem ->
                menuItem.foodName!!.lowercase().contains(query.lowercase()) ||
                        menuItem.foodPrice!!.lowercase().contains(query.lowercase())
            }
        }

}
