package com.example.flavorfiesta.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieDrawable
import com.example.flavorfiesta.Adapter.CartAdapter
import com.example.flavorfiesta.Models.CartItems
import com.example.flavorfiesta.PayOutActivity
import com.example.flavorfiesta.R
import com.example.flavorfiesta.databinding.FragmentCartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var foodNames: MutableList<String>
    private lateinit var foodPrices: MutableList<String>
    private lateinit var foodDescriptions: MutableList<String>
    private lateinit var foodImages: MutableList<String>
    private lateinit var foodQuantities: MutableList<Int>
    private lateinit var foodIngrediants: MutableList<String>
    private lateinit var cartAdapter: CartAdapter
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        retrieveCartItems()

        binding.proceedBtn.setOnClickListener {
            getCartItemDetail()
        }

        return binding.root
    }

    private fun getCartItemDetail() {
        val orderIdRef: DatabaseReference =
            database.reference.child("user").child(userId).child("CartItems")

        val foodName = mutableListOf<String>()
        val foodPrice = mutableListOf<String>()
        val foodDesc = mutableListOf<String>()
        val foodImg = mutableListOf<String>()
        val foodIngrediant = mutableListOf<String>()
        val foodQuantity = CartAdapter.getUpdatedItemsQuantities().toMutableList()

        orderIdRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapShot in snapshot.children) {
                    val orderItems = foodSnapShot.getValue(CartItems::class.java)

                    orderItems?.foodName?.let { foodName.add(it) }
                    orderItems?.foodPrice?.let { foodPrice.add(it) }
                    orderItems?.foodDescription?.let { foodDesc.add(it) }
                    orderItems?.foodImage?.let { foodImg.add(it) }
                    orderItems?.foodQuantity?.let { foodQuantity.add(it) }
                    orderItems?.foodIngrediant?.let { foodIngrediant.add(it) }
                }
                orderNow(foodName, foodPrice, foodDesc, foodImg, foodQuantity, foodIngrediant)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "Order making is Failed! Please try again!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun orderNow(
        foodName: MutableList<String>,
        foodPrice: MutableList<String>,
        foodDesc: MutableList<String>,
        foodImg: MutableList<String>,
        foodQuantity: MutableList<Int>,
        foodIngrediant: MutableList<String>
    ) {
        if (isAdded && context != null) {
            val intent = Intent(requireContext(), PayOutActivity::class.java)
            intent.putExtra("FoodItemName", ArrayList(foodName))
            intent.putExtra("FoodItemPrice", ArrayList(foodPrice))
            intent.putExtra("FoodItemDesc", ArrayList(foodDesc))
            intent.putExtra("FoodItemImg", ArrayList(foodImg))
            intent.putExtra("FoodItemIngrediant", ArrayList(foodIngrediant))
            intent.putExtra("FoodItemQuantity", ArrayList(foodQuantity))
            startActivity(intent)
        }
    }

    private fun retrieveCartItems() {
        userId = auth.currentUser?.uid ?: ""
        val foodRef: DatabaseReference =
            database.reference.child("user").child(userId).child("CartItems")

        foodNames = mutableListOf()
        foodPrices = mutableListOf()
        foodDescriptions = mutableListOf()
        foodImages = mutableListOf()
        foodQuantities = mutableListOf()
        foodIngrediants = mutableListOf()

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapShot in snapshot.children) {
                    val cartItems = foodSnapShot.getValue(CartItems::class.java)

                    cartItems?.foodName?.let { foodNames.add(it) }
                    cartItems?.foodPrice?.let { foodPrices.add(it) }
                    cartItems?.foodDescription?.let { foodDescriptions.add(it) }
                    cartItems?.foodQuantity?.let { foodQuantities.add(it) }
                    cartItems?.foodIngrediant?.let { foodIngrediants.add(it) }

                    // Use the safe call operator ?. and the safe cast operator as?
                    val image: String? = cartItems?.foodImage as? String

                    // Check if image is not null before adding to the list
                    if (image != null) {
                        foodImages.add(image)
                    }
                }
                binding.proceedBtn.visibility = View.INVISIBLE
                setAdapter()
            }

            private fun setAdapter() {
                if (foodNames.isNotEmpty() && foodPrices.isNotEmpty() && foodDescriptions.isNotEmpty() &&
                    foodIngrediants.isNotEmpty() || foodQuantities.isNotEmpty() || foodImages.isNotEmpty()
                ) {
                    cartAdapter = CartAdapter(
                        requireContext(),
                        foodNames,
                        foodPrices,
                        foodDescriptions,
                        foodImages,
                        foodQuantities,
                        foodIngrediants
                    )
                    binding.proceedBtn.visibility = View.VISIBLE
                    binding.recyclerView.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    binding.recyclerView.adapter = cartAdapter

                    // Hide animation view if it's visible
                    binding.emptyCartAnimationView.visibility = View.GONE
                } else {
                    // Show animation view and hide RecyclerView
                    binding.emptyCartAnimationView.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                    binding.proceedBtn.visibility = View.INVISIBLE
                    // Set up LottieAnimationView for the empty cart animation
                    binding.emptyCartAnimationView.setAnimation(R.raw.empty_cart)
                    binding.emptyCartAnimationView.repeatCount = LottieDrawable.INFINITE
                    binding.emptyCartAnimationView.playAnimation()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Data can't be fetched!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

