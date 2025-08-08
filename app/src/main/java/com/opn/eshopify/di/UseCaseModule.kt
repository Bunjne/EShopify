package com.opn.eshopify.di

import com.opn.eshopify.domain.usecase.GetProductsUseCase
import com.opn.eshopify.domain.usecase.GetStoreDetailUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    singleOf(::GetStoreDetailUseCase)
    singleOf(::GetProductsUseCase)
}
