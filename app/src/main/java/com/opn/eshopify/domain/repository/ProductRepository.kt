package com.opn.eshopify.domain.repository

import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.domain.model.Product

interface ProductRepository {

    suspend fun getProducts(): Result<List<Product>, DataError>
}
