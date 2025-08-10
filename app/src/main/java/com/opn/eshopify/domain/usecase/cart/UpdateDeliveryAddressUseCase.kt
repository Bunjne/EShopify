package com.opn.eshopify.domain.usecase.cart

import com.opn.eshopify.domain.repository.CartRepository

class UpdateDeliveryAddressUseCase(private val cartRepository: CartRepository) {

    operator fun invoke(address: String) {
        cartRepository.updateDeliveryAddress(address)
    }
}
