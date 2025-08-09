package com.opn.eshopify.presentation.store

import com.opn.eshopify.domain.usecase.GetProductsUseCase
import com.opn.eshopify.domain.usecase.GetStoreDetailUseCase
import com.opn.eshopify.domain.usecase.PlaceOrderUseCase

class StoreDetailUseCases(
    val getStoreDetail: GetStoreDetailUseCase,
    val getProducts: GetProductsUseCase,
    val placeOrder: PlaceOrderUseCase
)
