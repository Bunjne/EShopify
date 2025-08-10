package com.opn.eshopify.domain.usecase.cart

import com.opn.eshopify.domain.model.Product
import com.opn.eshopify.domain.repository.CartRepository

class AddProductToCartUseCase(private val cartRepository: CartRepository) {

    operator fun invoke(product: Product) {
        cartRepository.incrementProductQuantity(product)
    }
}
