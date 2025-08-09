package com.opn.eshopify.data.repository

import com.opn.eshopify.data.extension.safeApiCall
import com.opn.eshopify.data.remote.api.ShoppingAPI
import com.opn.eshopify.data.remote.mapper.asDto
import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.domain.model.Order
import com.opn.eshopify.domain.repository.OrderRepository
import com.opn.eshopify.domain.util.AppDispatchers

class DefaultOrderRepository(
    private val api: ShoppingAPI,
    private val appDispatchers: AppDispatchers
) : OrderRepository {
    
    override suspend fun placeOrder(order: Order): Result<Unit, DataError> {
        return safeApiCall(appDispatchers.getIODispatcher()) {
            api.makeOrder(order.asDto())
        }
    }
}
