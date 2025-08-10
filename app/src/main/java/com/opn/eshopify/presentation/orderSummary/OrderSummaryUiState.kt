package com.opn.eshopify.presentation.orderSummary

import com.opn.eshopify.domain.model.Product
import com.opn.eshopify.presentation.util.TextValue

data class OrderSummaryUiState(
    val selectedProducts: Map<Product, Int> = emptyMap(),
    val deliveryAddress: String = "",
    val totalPrice: Double = 0.0,
    val hasSelectedProducts: Boolean = false,
    val isLoading: Boolean = false,
    val error: TextValue? = null,
    val isOrderPlaced: Boolean = false
)
