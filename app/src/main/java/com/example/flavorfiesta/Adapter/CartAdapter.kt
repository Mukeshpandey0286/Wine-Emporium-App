package com.example.flavorfiesta.Adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flavorfiesta.databinding.CartItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CartAdapter(
    private var context: Context,
    private var cartItemName: MutableList<String>,
    private var cartItemPrice: MutableList<String>,
    private var cartItemDesc: MutableList<String>,
    private var cartItemImg: MutableList<String>,
    private var cartQuantity: MutableList<Int>,
    private var cartIngrediants: MutableList<String>

) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    //instance of firebase auth
    private val auth = FirebaseAuth.getInstance()

    init {
        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid ?: ""
        val cartItemNo = cartItemName.size

        itemQuantities = IntArray(cartItemNo) { 1 }
        cartItemRef = database.reference.child("user").child(userId).child("CartItems")

    }

    companion object {
        fun getUpdatedItemsQuantities(): IntArray {
            return itemQuantities
        }

        private var itemQuantities: IntArray = intArrayOf()
        private lateinit var cartItemRef: DatabaseReference
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = cartItemName.size
//    get updated quantity

//    fun getUpdatedItemsQuantities(): MutableList<Int> {
//        val itemQuantity = mutableListOf<Int>()
//        itemQuantity.addAll(cartQuantity)
//        return itemQuantity
//    }

    inner class CartViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
//            val cartItem = cartItems[position]
            binding.apply {

                val itemQuantity = itemQuantities[position]
                dishName.text = cartItemName[position]
                Log.d("cart", "cart: ${cartItemName}, ${cartItemPrice}")
                dishPrice.text = cartItemPrice[position]

//                load image with glide
                val uriString = cartItemImg[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(dishImage)
                cartItemsQuantity.text = itemQuantity.toString()


                minusBtn.setOnClickListener {
                    decreaseQuantity(position)
                }
                plusBtn.setOnClickListener {
                    increaseQuantity(position)
                }
                removeBtn.setOnClickListener {
                    val itemPosition = adapterPosition
                    if (itemPosition != RecyclerView.NO_POSITION) {
                        deleteItems(itemPosition)
                    }
                }

            }
        }

        private fun decreaseQuantity(position: Int) {
            if (position > 1) {
                itemQuantities[position]--
                cartQuantity[position] = itemQuantities[position]
                binding.cartItemsQuantity.text = itemQuantities[position].toString()
            }
        }

        private fun increaseQuantity(position: Int) {
            if (position < 10) {
                itemQuantities[position]++
                cartQuantity[position] = itemQuantities[position]
                binding.cartItemsQuantity.text = itemQuantities[position].toString()
            }
        }

        private fun deleteItems(position: Int) {
            val positionRetrieve = position
            getUniqueKeyAtPosition(positionRetrieve) { uniqueKey ->
                if (uniqueKey != null) {
                    removeItem(position, uniqueKey)
                }
            }
        }


        private fun removeItem(position: Int, uniqueKey: String) {
            cartItemRef.child(uniqueKey).removeValue().addOnSuccessListener {
                // Remove the item from the local lists and update quantities
                cartItemName.removeAt(position)
                cartItemPrice.removeAt(position)
                cartItemDesc.removeAt(position)
                cartItemImg.removeAt(position)
                cartQuantity.removeAt(position)
                cartIngrediants.removeAt(position)
                itemQuantities = itemQuantities.filterIndexed { index, i -> index != position }.toIntArray()

                Toast.makeText(context, "Item deleted🤷‍♂️", Toast.LENGTH_SHORT).show()

                notifyItemRemoved(position)
                notifyItemRangeChanged(position, cartItemName.size)
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to delete🤷‍♂️", Toast.LENGTH_SHORT).show()
            }
        }

        private fun getUniqueKeyAtPosition(positionRetrieve: Int, onComplete: (String?) -> Unit) {
            cartItemRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var uniqueKey: String? = null
//                    loop for snapshot childrens
                    snapshot.children.forEachIndexed { index, dataSnapshot ->
                        if (index == positionRetrieve) {
                            uniqueKey = dataSnapshot.key
                            return@forEachIndexed
                        }
                    }
                    onComplete(uniqueKey)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }
}
