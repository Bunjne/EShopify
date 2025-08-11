package com.opn.eshopify.mock.repository

import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.domain.model.Product
import com.opn.eshopify.domain.repository.ProductRepository

class FakeProductRepository : ProductRepository {

    override suspend fun getProducts(): Result<List<Product>, DataError> {
        return Result.Success(
            listOf(
                Product("1", "Apple", 2.5, ""),
                Product("2", "Banana", 1.25, "")
            )
        )
    }
}

