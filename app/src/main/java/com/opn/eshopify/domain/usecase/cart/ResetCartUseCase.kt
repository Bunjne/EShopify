package com.opn.eshopify.domain.usecase.cart

import com.opn.eshopify.domain.repository.CartRepository

class ResetCartUseCase(private val cartRepository: CartRepository) {

    operator fun invoke() {
        cartRepository.reset()
    }
}
