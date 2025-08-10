package com.opn.eshopify.data.repository

import com.opn.eshopify.domain.model.Product
import com.opn.eshopify.domain.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DefaultCartRepository : CartRepository {
    private val _selectedProducts = MutableStateFlow<Map<Product, Int>>(emptyMap())
    override val selectedProducts: StateFlow<Map<Product, Int>> = _selectedProducts.asStateFlow()
    
    private val _deliveryAddress = MutableStateFlow("")
    override val deliveryAddress: StateFlow<String> = _deliveryAddress.asStateFlow()
    
    override fun incrementProductQuantity(product: Product) {
        _selectedProducts.update { currentProducts ->
            val currentQuantity = currentProducts[product] ?: 0
            currentProducts.toMutableMap().apply {
                put(product, currentQuantity + 1)
            }
        }
    }
    
    override fun decrementProductQuantity(product: Product) {
        _selectedProducts.update { currentProducts ->
            val currentQuantity = currentProducts[product] ?: 0
            val updatedProducts = currentProducts.toMutableMap()
            if (currentQuantity <= 1) {
                updatedProducts.remove(product)
            } else {
                updatedProducts[product] = currentQuantity - 1
            }
            updatedProducts
        }
    }
    
    override fun updateDeliveryAddress(address: String) {
        _deliveryAddress.update { address }
    }

    override fun reset() {
        _selectedProducts.value = emptyMap()
        _deliveryAddress.value = ""
    }
}
