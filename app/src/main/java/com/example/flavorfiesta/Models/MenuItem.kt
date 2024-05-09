package com.example.flavorfiesta.Models

data class MenuItem(
    val foodName: String ?=null,
    val foodPrice: String ?=null,
    val foodDescription: String ?=null,
    val foodImage: String ?=null,
    val foodIngrediant: String ?=null,
    val restroName : String ?= null
)
