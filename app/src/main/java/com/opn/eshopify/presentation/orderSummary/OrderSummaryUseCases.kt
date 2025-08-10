package com.opn.eshopify.presentation.orderSummary

import com.opn.eshopify.domain.usecase.cart.PlaceOrderUseCase
import com.opn.eshopify.domain.usecase.cart.GetCartDetailUseCase
import com.opn.eshopify.domain.usecase.cart.UpdateDeliveryAddressUseCase

class OrderSummaryUseCases(
    val placeOrder: PlaceOrderUseCase,
    val updateDeliveryAddress: UpdateDeliveryAddressUseCase,
    val getCartDetail: GetCartDetailUseCase,
)
