package com.opn.eshopify.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoreDto(
    @SerialName("name") val name: String,
    @SerialName("rating") val rating: Float,
    @SerialName("openingTime") val openingTime: String,
    @SerialName("closingTime") val closingTime: String,
)
