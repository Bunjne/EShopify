package com.opn.eshopify.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderDto(
    @SerialName("products") val products: List<ProductDto>,
    @SerialName("delivery_address") val deliveryAddress: Double,
)
