package com.opn.eshopify.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("price") val price: Double,
    @SerialName("imageUrl") val imageUrl: String,
)
