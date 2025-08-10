package com.opn.eshopify.domain.repository

import com.opn.eshopify.domain.model.Product
import kotlinx.coroutines.flow.StateFlow

interface CartRepository {
    val selectedProducts: StateFlow<Map<Product, Int>>
    val deliveryAddress: StateFlow<String>
    
    fun incrementProductQuantity(product: Product)
    fun decrementProductQuantity(product: Product)
    fun updateDeliveryAddress(address: String)
    fun reset()
}
