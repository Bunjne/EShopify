package com.opn.eshopify.domain.usecase

import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.domain.model.Product
import com.opn.eshopify.domain.repository.ProductRepository

class GetProductsUseCase(private val repository: ProductRepository) {

    suspend operator fun invoke(): Result<List<Product>, DataError> = repository.getProducts()
}
