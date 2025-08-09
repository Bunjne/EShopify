package com.opn.eshopify.data.repository

import com.opn.eshopify.data.extension.safeApiCall
import com.opn.eshopify.data.remote.api.ShoppingAPI
import com.opn.eshopify.data.remote.mapper.asDomain
import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.domain.model.Product
import com.opn.eshopify.domain.repository.ProductRepository
import com.opn.eshopify.util.AppDispatchers

class DefaultProductRepository(
    private val api: ShoppingAPI,
    private val appDispatchers: AppDispatchers,
) : ProductRepository {

    override suspend fun getProducts(): Result<List<Product>, DataError> {
        return safeApiCall(appDispatchers.getIODispatcher()) {
            api.getProducts().mapIndexed { index, productDto ->
                productDto.asDomain(index.toString())
            }
        }
    }
}
