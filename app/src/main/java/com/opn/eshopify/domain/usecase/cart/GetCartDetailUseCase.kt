package com.opn.eshopify.domain.usecase.cart

import com.opn.eshopify.domain.model.Cart
import com.opn.eshopify.domain.model.Product
import com.opn.eshopify.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetCartDetailUseCase(private val cartRepository: CartRepository) {

    operator fun invoke(): Flow<Cart> {
        return combine(
            cartRepository.selectedProducts,
            cartRepository.deliveryAddress
        ) { products, address ->
            Cart(
                products = products,
                deliveryAddress = address,
                totalPrice = calculateTotalPrice(products),
                hasProducts = products.isNotEmpty()
            )
        }
    }
    
    private fun calculateTotalPrice(products: Map<Product, Int>): Double {
        return products.entries.sumOf { (product, quantity) -> 
            product.price * quantity 
        }
    }
}

