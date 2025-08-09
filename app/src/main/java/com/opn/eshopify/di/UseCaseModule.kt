package com.opn.eshopify.di

import com.opn.eshopify.domain.usecase.GetProductsUseCase
import com.opn.eshopify.domain.usecase.GetStoreDetailUseCase
import com.opn.eshopify.domain.usecase.PlaceOrderUseCase
import com.opn.eshopify.presentation.store.StoreDetailUseCases
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    singleOf(::GetStoreDetailUseCase)
    singleOf(::GetProductsUseCase)
    singleOf(::PlaceOrderUseCase)
    singleOf(::StoreDetailUseCases)
}
