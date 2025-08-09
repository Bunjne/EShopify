package com.opn.eshopify.domain.usecase

import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.domain.model.Order
import com.opn.eshopify.domain.repository.OrderRepository

class PlaceOrderUseCase(private val orderRepository: OrderRepository) {
    suspend operator fun invoke(order: Order): Result<Unit, DataError> {
        return orderRepository.placeOrder(order)
    }
}
