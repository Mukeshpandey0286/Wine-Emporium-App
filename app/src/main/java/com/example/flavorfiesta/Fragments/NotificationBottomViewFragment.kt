package com.example.flavorfiesta.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flavorfiesta.Adapter.NotificationAdapter
import com.example.flavorfiesta.R
import com.example.flavorfiesta.databinding.FragmentNotificationBottomViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class NotificationBottomViewFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNotificationBottomViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBottomViewBinding.inflate(inflater,container,false)

        binding.arrowBck.setOnClickListener {
            dismiss()
        }

        val notification = arrayListOf("Your order has been Canceled Successfully", "Order has been taken by the driver", "Congrats Your Order Placed")
        val img = arrayListOf(R.drawable.sademoji, R.drawable.truck, R.drawable.illustration)
        val adapter = NotificationAdapter(notification,img)
        binding.notificationRcv.layoutManager = LinearLayoutManager(requireContext())
        binding.notificationRcv.adapter = adapter

        return binding.root
    }

    companion object {

    }
}