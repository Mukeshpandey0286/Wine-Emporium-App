package com.example.flavorfiesta.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flavorfiesta.databinding.HistoryItemsBinding


class HistoryAdapter(
    private val buyAgainFoodName: MutableList<String>,
    private val buyAgainFoodPrice: MutableList<String>,
    private val buyAgainFoodImg: MutableList<String>,
    private val requireContext: Context,
) : RecyclerView.Adapter<HistoryAdapter.HistoryItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        val binding = HistoryItemsBinding.inflate(LayoutInflater.from(requireContext), parent, false)
        return HistoryItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        holder.bind(
            buyAgainFoodName[position],
            buyAgainFoodPrice[position],
            buyAgainFoodImg[position]
        )
    }

    override fun getItemCount(): Int {
        return buyAgainFoodName.size
    }

    inner class HistoryItemViewHolder(val binding: HistoryItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(foodName: String, foodPrice: String, foodImg: String) {

            binding.apply {
                dishName.text = foodName
                dishPrice.text = foodPrice
                val uriString = foodImg
                val uri = Uri.parse(uriString)
                Glide.with(requireContext).load(uri).into(dishImage)
            }
        }

    }

}