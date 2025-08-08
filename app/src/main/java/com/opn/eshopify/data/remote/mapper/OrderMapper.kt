package com.opn.eshopify.data.remote.mapper

import android.R.attr.description
import android.R.attr.name
import com.opn.eshopify.data.remote.model.OrderDto
import com.opn.eshopify.data.remote.model.ProductDto
import com.opn.eshopify.domain.model.Order
import com.opn.eshopify.domain.model.Product

fun Order.toDto(): OrderDto = OrderDto(
    products = products.map {
        it.toDto()
    },
    deliveryAddress = deliveryAddress
)
