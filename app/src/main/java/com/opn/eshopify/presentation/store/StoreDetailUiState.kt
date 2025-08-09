package com.opn.eshopify.presentation.store

import com.opn.eshopify.domain.model.Product
import com.opn.eshopify.domain.model.Store
import com.opn.eshopify.util.TextValue

data class StoreDetailUiState(
    val store: Store? = null,
    val products: List<Product> = emptyList(),
    val selectedProducts: Map<Product, Int> = emptyMap(),
    val isLoading: Boolean = false,
    val error: TextValue? = null,
    val deliveryAddress: String = "",
    val isOrderPlaced: Boolean = false
) {
    val totalPrice: Double
        get() = selectedProducts.entries.sumOf { (product, quantity) ->
            product.price * quantity
        }

    val hasSelectedProducts: Boolean
        get() = selectedProducts.isNotEmpty()
}
