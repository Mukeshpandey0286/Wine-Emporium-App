package com.example.flavorfiesta.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flavorfiesta.DetailsActivity
import com.example.flavorfiesta.databinding.PopularItemBinding

class PopularAdapter (private val items: List<String>, private val  prices:List<String>, private val image:List<Int>, private val requireContext: Context):RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
       return PopularViewHolder(PopularItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        val item = items[position]
        val images = image[position]
        val price = prices[position]
        holder.bind(item,price,images)

        holder.itemView.setOnClickListener {
            // Setonclicklistener to open details
            val intent = Intent(requireContext, DetailsActivity::class.java)
            intent.putExtra("MenuItemName", item)
            intent.putExtra("MenuItemImage", images)
            requireContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class PopularViewHolder(private val binding: PopularItemBinding): RecyclerView.ViewHolder(binding.root) {
      private  val imageview = binding.popularFoodImg
        fun bind(item: String, price: String,images: Int) {
            binding.popularFoodName.text = item
            binding.popularFoodAmmount.text = price
            imageview.setImageResource(images)
        }

    }
}