package com.example.flavorfiesta.Models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.auth.FirebaseAuth
import java.io.Serializable
import java.util.ArrayList

class OrderDetails() :Serializable {
    var userUid : String? = null
    var userName : String? = null
    var foodNames : MutableList<String>? = null
    var foodImages : MutableList<String>? = null
//    var foodRestro : MutableList<String>? = null
    var foodPrices : MutableList<String>? = null
    var foodQuantities : MutableList<Int>? = null
    var address : String? = null
    var totalAmount : String? = null
    var phoneNumber : String? = null
    var orderAccepted : Boolean = false
    var paymentReceived : Boolean = false
    var itemPushKey : String? = null
    var currentTime : Long = 0

    constructor(parcel: Parcel) : this() {
        userUid = parcel.readString()
        userName = parcel.readString()
        address = parcel.readString()
        totalAmount = parcel.readString()
        phoneNumber = parcel.readString()
        orderAccepted = parcel.readByte() != 0.toByte()
        paymentReceived = parcel.readByte() != 0.toByte()
        itemPushKey = parcel.readString()
        currentTime = parcel.readLong()
    }

    constructor(
        userId: String,
        name: String,
        foodItemName: ArrayList<String>,
        foodItemImg: ArrayList<String>,
        foodItemPrice: ArrayList<String>,
//        foodRestro: ArrayList<String>,
        foodItemQuantity: ArrayList<Int>,
        address: String,
        totalAmount: String,
        phone: String,
        time: Long,
        b: Boolean,
        b1: Boolean,
        itemPushKey: String?,
        i: Int
    )
    : this(){
        this.userUid = userId
        this.userName = name
        this.foodNames = foodItemName
        this.foodImages = foodItemImg
//        this.foodRestro = foodRestro
        this.foodPrices = foodItemPrice
        this.foodQuantities = foodItemQuantity
        this.address = address
        this.totalAmount = totalAmount
        this.phoneNumber = phone
        this.orderAccepted = b
        this.paymentReceived = b1
        this.itemPushKey = itemPushKey
        this.currentTime = time
    }


     fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userUid)
        parcel.writeString(userName)
        parcel.writeString(address)
        parcel.writeString(totalAmount)
        parcel.writeString(phoneNumber)
        parcel.writeByte(if (orderAccepted) 1 else 0)
        parcel.writeByte(if (paymentReceived) 1 else 0)
        parcel.writeString(itemPushKey)
        parcel.writeLong(currentTime)
    }

     fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderDetails> {
        override fun createFromParcel(parcel: Parcel): OrderDetails {
            return OrderDetails(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetails?> {
            return arrayOfNulls(size)
        }
    }


}