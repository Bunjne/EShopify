package com.opn.eshopify.domain.model

data class PaginatedResult<T>(
    val items: List<T>,
    val currentPage: Int,
    val totalPages: Int,
    val totalCount: Int,
    val hasNextPage: Boolean
)

