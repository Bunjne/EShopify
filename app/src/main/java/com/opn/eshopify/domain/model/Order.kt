package com.opn.eshopify.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Order(
    val products: List<Product>,
    val deliveryAddress: Double,
)
