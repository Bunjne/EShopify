package com.opn.eshopify.domain.usecase.store

import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.domain.model.PaginatedResult
import com.opn.eshopify.domain.model.Product
import com.opn.eshopify.domain.repository.ProductRepository

class GetProductsUseCase(private val repository: ProductRepository) {

    suspend operator fun invoke(
        page: Int,
        limit: Int
    ): Result<PaginatedResult<Product>, DataError> = repository.getProducts(page, limit)
}
