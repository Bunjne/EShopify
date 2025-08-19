package com.opn.eshopify.mock.repository

import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.domain.model.PaginatedResult
import com.opn.eshopify.domain.model.Product
import com.opn.eshopify.domain.repository.ProductRepository

class FakeProductRepository : ProductRepository {

    private val allProducts = listOf(
        Product("1", "Apple", 2.5, ""),
        Product("2", "Banana", 1.25, ""),
        Product("3", "Orange", 3.0, ""),
        Product("4", "Grape", 4.5, ""),
        Product("5", "Mango", 5.0, "")
    )

    override suspend fun getProducts(page: Int, limit: Int): Result<PaginatedResult<Product>, DataError> {
        val startIndex = (page - 1) * limit
        val endIndex = minOf(startIndex + limit, allProducts.size)
        
        if (startIndex >= allProducts.size) {
            return Result.Success(
                PaginatedResult(
                    items = emptyList(),
                    currentPage = page,
                    totalPages = (allProducts.size + limit - 1) / limit,
                    totalCount = allProducts.size,
                    hasNextPage = false
                )
            )
        }
        
        val pageProducts = allProducts.subList(startIndex, endIndex)
        val totalPages = (allProducts.size + limit - 1) / limit
        
        return Result.Success(
            PaginatedResult(
                items = pageProducts,
                currentPage = page,
                totalPages = totalPages,
                totalCount = allProducts.size,
                hasNextPage = page < totalPages
            )
        )
    }
}

