package com.opn.eshopify.presentation.store

import com.opn.eshopify.domain.model.Product
import com.opn.eshopify.domain.model.Store
import com.opn.eshopify.presentation.util.TextValue

data class StoreDetailUiState(
    val store: Store? = null,
    val products: List<Product> = emptyList(),
    val selectedProducts: Map<Product, Int> = emptyMap(),
    val deliveryAddress: String = "",
    val totalPrice: Double = 0.0,
    val hasSelectedProducts: Boolean = false,
    val isLoading: Boolean = false,
    val error: TextValue? = null,
    val hasMorePages: Boolean = false,
    val currentPage: Int = 1,
    val isLoadingMore: Boolean = false,
    val paginationError: TextValue? = null,
    val isRefreshing: Boolean = false,
) {

    val canLoadMore: Boolean
        get() = hasMorePages &&
                !isLoadingMore &&
                !isRefreshing &&
                paginationError == null
}
