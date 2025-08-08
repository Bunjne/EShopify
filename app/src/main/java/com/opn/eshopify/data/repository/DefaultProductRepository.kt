package com.opn.eshopify.data.repository

import com.opn.eshopify.data.extension.safeApiCall
import com.opn.eshopify.data.remote.api.ShoppingAPI
import com.opn.eshopify.data.remote.mapper.toDomain
import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.domain.model.Product
import com.opn.eshopify.domain.repository.ProductRepository

class DefaultProductRepository(private val api: ShoppingAPI) : ProductRepository {

    override suspend fun getProducts(): Result<List<Product>, DataError> {
        return safeApiCall {
            api.getProducts().map { it.toDomain() }
        }
    }
}
