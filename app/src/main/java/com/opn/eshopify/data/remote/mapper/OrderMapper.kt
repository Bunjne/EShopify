package com.opn.eshopify.data.remote.mapper

import com.opn.eshopify.data.remote.model.OrderDto
import com.opn.eshopify.domain.model.Order

fun Order.asDto(): OrderDto = OrderDto(
    products = products.map {
        it.asDto()
    },
    deliveryAddress = deliveryAddress
)
