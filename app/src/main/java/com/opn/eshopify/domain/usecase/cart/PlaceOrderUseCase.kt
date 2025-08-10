package com.opn.eshopify.domain.usecase.cart

import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.domain.model.Order
import com.opn.eshopify.domain.repository.CartRepository
import com.opn.eshopify.domain.repository.OrderRepository

class PlaceOrderUseCase(
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository,
) {
    suspend operator fun invoke(): Result<Unit, DataError> {
        val products = cartRepository.selectedProducts.value.keys.toList()
        val address = cartRepository.deliveryAddress.value

        val order = Order(
            products = products,
            deliveryAddress = address
        )

        val result = orderRepository.placeOrder(order)

        if (result is Result.Success) {
            cartRepository.reset()
        }

        return result
    }
}
