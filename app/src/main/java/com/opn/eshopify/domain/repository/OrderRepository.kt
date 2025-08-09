package com.opn.eshopify.domain.repository

import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.domain.model.Order

interface OrderRepository {
    suspend fun placeOrder(order: Order): Result<Unit, DataError>
}
