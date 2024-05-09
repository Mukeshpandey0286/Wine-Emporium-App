package com.example.flavorfiesta

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.flavorfiesta.Models.CartItems
import com.example.flavorfiesta.databinding.ActivityDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailsBinding
    private  var foodName: String ?=null
    private  var foodPrice: String ?=null
    private  var foodDescription: String ?=null
    private  var foodIngrediant: String ?=null
    private  var foodImage: String ?=null
    private  var restroName : String ?= null
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        foodName = intent.getStringExtra("menuItemName")
        foodDescription = intent.getStringExtra("menuItemDescription")
        foodIngrediant = intent.getStringExtra("menuDishIngrediants")
        foodPrice = intent.getStringExtra("menuItemPrice")
        foodImage = intent.getStringExtra("menuItemImage")
        restroName = intent.getStringExtra("menuRestroName")

        with(binding){
            detailFoodName.text = foodName
            detailFoodDesc.text = foodDescription
            restroo.text = "with ${restroName}"
            ingrediantdetail.text = foodIngrediant
            Glide.with(this@DetailsActivity).load(Uri.parse(foodImage)).into(detailFoodImg)

        }

        binding.arrwBck.setOnClickListener {
            finish()
        }

        binding.addItemBtn.setOnClickListener {
            val database = FirebaseDatabase.getInstance().reference
            val userId = auth.currentUser?.uid?:""
//            create a cart item objects
            val cartItem = CartItems(foodName.toString(),foodPrice.toString(),foodDescription.toString(),foodImage.toString(),1)

//            save data to cart item to from database
            database.child("user").child(userId).child("CartItems").push().setValue(cartItem).addOnSuccessListener {
                Toast.makeText(this, "Item added into Cart successfully❤😁",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(this, "Item not added to Cart😒",Toast.LENGTH_SHORT).show()
            }

        }
    }
}