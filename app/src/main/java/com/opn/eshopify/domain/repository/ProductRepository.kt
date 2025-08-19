package com.opn.eshopify.domain.repository

import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.domain.model.PaginatedResult
import com.opn.eshopify.domain.model.Product

interface ProductRepository {

    suspend fun getProducts(
        page: Int,
        limit: Int
    ): Result<PaginatedResult<Product>, DataError>
}
