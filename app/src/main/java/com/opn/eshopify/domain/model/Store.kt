package com.opn.eshopify.domain.model

import kotlinx.datetime.LocalTime

data class Store(
    val name: String,
    val rating: Float,
    val openingTime: LocalTime,
    val closingTime: LocalTime,
)
