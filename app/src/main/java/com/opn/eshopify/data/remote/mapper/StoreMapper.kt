package com.opn.eshopify.data.remote.mapper

import com.opn.eshopify.data.extension.parseISOTime
import com.opn.eshopify.data.remote.model.StoreDto
import com.opn.eshopify.domain.model.Store

fun StoreDto.asDomain(): Store = Store(
    name = name,
    rating = rating,
    openingTime = parseISOTime(openingTime),
    closingTime = parseISOTime(closingTime)
)
