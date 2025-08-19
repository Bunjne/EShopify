package com.opn.eshopify.data.remote.mapper

import com.opn.eshopify.data.remote.model.ProductDto
import com.opn.eshopify.data.remote.model.ProductResponse
import com.opn.eshopify.domain.model.PaginatedResult
import com.opn.eshopify.domain.model.Product

fun ProductDto.asDomain(): Product = Product(
    id = id.toString(),
    name = name,
    price = price,
    imageUrl = imageUrl,
)

fun Product.asDto(): ProductDto = ProductDto(
    id = id.toInt(),
    name = name,
    price = price,
    imageUrl = imageUrl,
)

fun ProductResponse.asDomain(): PaginatedResult<Product> {
    val products = data.productResult.products.map { it.asDomain() }
    val pagination = data.productResult.pagination
    
    return PaginatedResult(
        items = products,
        currentPage = pagination.currentPage,
        totalPages = pagination.totalPages,
        totalCount = pagination.totalCount,
        hasNextPage = pagination.currentPage < pagination.totalPages
    )
}
