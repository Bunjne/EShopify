package com.opn.eshopify.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    @SerialName("data") val data: ProductData
) {

    @Serializable
    data class ProductData(
        @SerialName("ProductResult") val productResult: ProductResult,
    )

    @Serializable
    data class ProductResult(
        @SerialName("Products") val products: List<ProductDto>,
        @SerialName("PaginationInfo") val pagination: PaginationInfo
    )

    @Serializable
    data class PaginationInfo(
        @SerialName("current_page") val currentPage: Int,
        @SerialName("total_pages") val totalPages: Int,
        @SerialName("total_count") val totalCount: Int,
    )
}
