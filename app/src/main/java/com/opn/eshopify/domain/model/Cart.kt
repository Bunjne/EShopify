package com.opn.eshopify.domain.model

data class Cart(
    val products: Map<Product, Int>,
    val deliveryAddress: String,
    val totalPrice: Double,
    val hasProducts: Boolean
)
