package com.opn.eshopify.data.remote.mapper

import com.opn.eshopify.data.remote.model.ProductDto
import com.opn.eshopify.domain.model.Product

fun ProductDto.asDomain(): Product = Product(
    name = name,
    price = price,
    imageUrl = imageUrl,
)

fun Product.asDto(): ProductDto = ProductDto(
    name = name,
    price = price,
    imageUrl = imageUrl,
)
