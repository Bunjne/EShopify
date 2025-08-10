package com.opn.eshopify.di

import com.opn.eshopify.domain.usecase.store.GetProductsUseCase
import com.opn.eshopify.domain.usecase.store.GetStoreDetailUseCase
import com.opn.eshopify.domain.usecase.cart.PlaceOrderUseCase
import com.opn.eshopify.domain.usecase.cart.AddProductToCartUseCase
import com.opn.eshopify.domain.usecase.cart.GetCartDetailUseCase
import com.opn.eshopify.domain.usecase.cart.RemoveProductFromCartUseCase
import com.opn.eshopify.domain.usecase.cart.ResetCartUseCase
import com.opn.eshopify.domain.usecase.cart.UpdateDeliveryAddressUseCase
import com.opn.eshopify.presentation.orderSummary.OrderSummaryUseCases
import com.opn.eshopify.presentation.store.StoreDetailUseCases
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    singleOf(::GetStoreDetailUseCase)
    singleOf(::GetProductsUseCase)

    singleOf(::AddProductToCartUseCase)
    singleOf(::RemoveProductFromCartUseCase)
    singleOf(::UpdateDeliveryAddressUseCase)
    singleOf(::GetCartDetailUseCase)
    singleOf(::ResetCartUseCase)
    singleOf(::PlaceOrderUseCase)

    singleOf(::StoreDetailUseCases)
    singleOf(::OrderSummaryUseCases)
}
