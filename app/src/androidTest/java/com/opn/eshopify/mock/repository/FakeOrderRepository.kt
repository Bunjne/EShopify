package com.opn.eshopify.mock.repository

import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.domain.model.Order
import com.opn.eshopify.domain.repository.OrderRepository

class FakeOrderRepository : OrderRepository {

    override suspend fun placeOrder(order: Order): Result<Unit, DataError> = Result.Success(Unit)
}
