package com.example.flavorfiesta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.flavorfiesta.Fragments.CongratulationFragment
import com.example.flavorfiesta.Models.OrderDetails
import com.example.flavorfiesta.databinding.ActivityPayOutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PayOutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPayOutBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var name: String
    private lateinit var address: String
    private lateinit var phone: String
    private lateinit var totalAmount: String
    private lateinit var foodItemName: ArrayList<String>

    //    private lateinit var foodRestro : ArrayList<String>
    private lateinit var foodItemPrice: ArrayList<String>
    private lateinit var foodItemDesc: ArrayList<String>
    private lateinit var foodItemImg: ArrayList<String>
    private lateinit var foodItemIngrediant: ArrayList<String>
    private lateinit var foodItemQuantity: ArrayList<Int>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        initialize firebase and user details
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference()

//        set user data
        setUserData()

//        get user details from database
        val intent = intent
        foodItemName = intent.getStringArrayListExtra("FoodItemName") as ArrayList<String>
        foodItemPrice = intent.getStringArrayListExtra("FoodItemPrice") as ArrayList<String>
        foodItemDesc = intent.getStringArrayListExtra("FoodItemDesc") as ArrayList<String>
        foodItemImg = intent.getStringArrayListExtra("FoodItemImg") as ArrayList<String>
        foodItemIngrediant =
            intent.getStringArrayListExtra("FoodItemIngrediant") as ArrayList<String>
        foodItemQuantity = intent.getIntegerArrayListExtra("FoodItemQuantity") as ArrayList<Int>

        totalAmount = calculateTotalAmount().toString() + "₹"
        binding.CusAmount.text = totalAmount
//        binding.CusAmount.setText((totalAmount))

        binding.orderPlaceBtn.setOnClickListener {
            name = binding.CusName.toString().trim()
            address = binding.CusAddress.toString().trim()
            phone = binding.CusPhone.toString().trim()
            if (name.isBlank() || address.isBlank() || phone.isBlank()) {
                Toast.makeText(this, "Please fill all the Details! 😒", Toast.LENGTH_SHORT).show()
            } else {
                placeOrder()
            }

        }
    }

    private fun placeOrder() {
        userId = auth.currentUser?.uid ?: ""
        val time = System.currentTimeMillis()
        val itemPushKey = databaseReference.child("OrderDetails").push().key
        val orderDetails = OrderDetails(
            userId,
            name,
            foodItemName,
            foodItemImg,
            foodItemPrice,
            foodItemQuantity,
            address,
            totalAmount,
            phone,
            time,
            false,
            false,
            itemPushKey,
            0
        )
        val orderRef = databaseReference.child("OrderDetails").child(itemPushKey!!)
        orderRef.setValue(orderDetails).addOnSuccessListener {
            val bottomSheetDialog = CongratulationFragment()
            bottomSheetDialog.show(supportFragmentManager, "Test")
            Toast.makeText(this, "Your order is successfully placed!😁", Toast.LENGTH_SHORT).show()
            removeItemFromCart()
            addOrderToHistory(orderDetails)
        }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to order!😥", Toast.LENGTH_SHORT).show()

            }
    }

    private fun addOrderToHistory(orderDetails: OrderDetails) {
        databaseReference.child("user").child(userId).child("BuyHistory")
            .child(orderDetails.itemPushKey!!).setValue(orderDetails)
            .addOnSuccessListener {

            }
    }

    private fun removeItemFromCart() {
        val cartItemsReference = databaseReference.child("user").child(userId).child("CartItems")
        cartItemsReference.removeValue()
    }

    private fun calculateTotalAmount(): Int {
        var totalAmount = 0
        for (i in foodItemPrice.indices) {
            var price = foodItemPrice[i]

            // Remove the currency symbol and parse the numeric part
            val numericPart = price.replace("₹", "")

            // Parse the numeric part to an integer
            val priceIntValue = if (numericPart.isNotBlank()) numericPart.toInt() else 0

            val quantity = foodItemQuantity[i]
            totalAmount += priceIntValue * quantity
        }
        return totalAmount
    }

    private fun setUserData() {
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            val userRef = databaseReference.child("user").child(userId)

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val names = snapshot.child("name").getValue(String::class.java) ?: ""
                        val addresss = snapshot.child("address").getValue(String::class.java) ?: ""
                        val phones = snapshot.child("phone").getValue(String::class.java) ?: ""

                        binding.apply {
                            CusName.setText(names)
                            CusAddress.setText(addresss)
                            CusPhone.setText(phones)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@PayOutActivity,
                        "Error while making Calculation!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }
    }
}