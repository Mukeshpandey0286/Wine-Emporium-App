package com.example.flavorfiesta.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flavorfiesta.databinding.NotificationItemViewBinding

class NotificationAdapter(private val notificationMsg : ArrayList<String>, private val notificationPic : ArrayList<Int>) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = NotificationItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(position)
    }


    override fun getItemCount(): Int {
        return notificationMsg.size
    }

    inner class NotificationViewHolder(private val binding: NotificationItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {

            binding.apply {
                notificationText.text = notificationMsg[position]
                notificationImg.setImageResource(notificationPic[position])
            }
        }

    }
}