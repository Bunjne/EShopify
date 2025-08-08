package com.opn.eshopify.data.remote.mapper

import com.opn.eshopify.data.remote.model.ProductDto
import com.opn.eshopify.domain.model.Product

fun ProductDto.toDomain(): Product = Product(
    name = name,
    price = price,
    imageUrl = imageUrl,
)

fun Product.toDto(): ProductDto = ProductDto(
    name = name,
    price = price,
    imageUrl = imageUrl,
)
