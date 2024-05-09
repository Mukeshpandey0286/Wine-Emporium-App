package com.example.flavorfiesta.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.flavorfiesta.Adapter.HistoryAdapter
import com.example.flavorfiesta.Models.OrderDetails
import com.example.flavorfiesta.RecentOrderItems
import com.example.flavorfiesta.databinding.FragmentHistoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HistoryFragment : Fragment() {
    private lateinit var binding : FragmentHistoryBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var auth : FirebaseAuth
    private lateinit var userId : String
    private var listOfOrderItems : MutableList<OrderDetails> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater,container,false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

//        retrieve user history data
        retrieveBuyHistory()

        binding.recentBuyItem.setOnClickListener {
            seeRecentItemsPurchased()
        }
        return binding.root
    }

    private fun seeRecentItemsPurchased() {
        listOfOrderItems.firstOrNull()?.let {recentBuy ->
            val intent = Intent(requireContext(), RecentOrderItems::class.java)
            intent.putExtra("RecentBuyOrderItem", ArrayList(listOfOrderItems))
            startActivity(intent)
        }
    }
    private fun retrieveBuyHistory() {
        binding.recentBuyItem.visibility = View.INVISIBLE
        userId = auth.currentUser?.uid ?: ""
        val buyItemRef: DatabaseReference =
            database.reference.child("user").child(userId).child("BuyHistory")
        val sortingQuery = buyItemRef.orderByChild("currentTime")
        sortingQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (buySnapshot in snapshot.children) {
                    val buyHistoryItem = buySnapshot.getValue(OrderDetails::class.java)
                    buyHistoryItem?.let {
                        listOfOrderItems.add(it)
                    }
                }
                listOfOrderItems.reverse()
                if (listOfOrderItems.isNotEmpty()) {
//                    display the most recent order details
                    setDataInRecentBuy()
//                    setup the rcv with previous order details
                    setPreviousBuyItemData()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
//    function to display the most recent order details
            private fun setDataInRecentBuy() {
                binding.recentBuyItem.visibility = View.VISIBLE
                val recentBuyOrder = listOfOrderItems.firstOrNull()
                recentBuyOrder?.let {
                    with(binding){
                        foodName.text = it.foodNames?.firstOrNull()?:""
                        foodPrice.text = it.foodPrices?.firstOrNull()?:""
                        val foodImage = it.foodImages?.firstOrNull()?:""
                        val uri = Uri.parse(foodImage)
                        Glide.with(requireContext()).load(uri).into(foodImg)

                        listOfOrderItems.reverse()
                        if (listOfOrderItems.isNotEmpty()) {

                        }
                    }
                }
            }

    //               function to setup the rcv with previous order details
            private fun setPreviousBuyItemData() {

                val buyAgainFoodName = mutableListOf<String>()
                val buyAgainFoodPrice = mutableListOf<String>()
                val buyAgainFoodImg = mutableListOf<String>()

                for(i in 1 until listOfOrderItems.size){
                    listOfOrderItems[i].foodNames?.firstOrNull()?.let { buyAgainFoodName.add(it)}
                    listOfOrderItems[i].foodPrices?.firstOrNull()?.let { buyAgainFoodPrice.add(it)}
                    listOfOrderItems[i].foodImages?.firstOrNull()?.let { buyAgainFoodImg.add(it)}
                }
                val rv = binding.priviousBuyrcv
                rv.layoutManager = LinearLayoutManager(requireContext())
                rv.adapter = HistoryAdapter(buyAgainFoodName,buyAgainFoodPrice,buyAgainFoodImg,requireContext())
            }
    }