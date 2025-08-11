package com.opn.eshopify.mock.repository

import com.opn.eshopify.domain.model.Product
import com.opn.eshopify.domain.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeCartRepository : CartRepository {
    private val _products = MutableStateFlow<Map<Product, Int>>(
        mapOf(
            Product("1", "Apple", 2.5, "") to 1,
            Product("2", "Banana", 1.25, "") to 2
        )
    )
    private val _address = MutableStateFlow("Bangkok")

    override val selectedProducts: StateFlow<Map<Product, Int>> = _products
    override val deliveryAddress: StateFlow<String> = _address

    override fun incrementProductQuantity(product: Product) {
        val q = (_products.value[product] ?: 0) + 1
        _products.value = _products.value.toMutableMap().apply { put(product, q) }
    }

    override fun decrementProductQuantity(product: Product) {
        val cur = (_products.value[product] ?: 0)
        val q = (cur - 1).coerceAtLeast(0)
        _products.value = _products.value.toMutableMap().apply {
            if (q == 0) remove(product) else put(product, q)
        }
    }

    override fun updateDeliveryAddress(address: String) {
        _address.value = address
    }

    override fun reset() {
        _products.value = emptyMap()
        _address.value = ""
    }
}
