package com.opn.eshopify.di

import com.opn.eshopify.presentation.orderSummary.OrderSummaryViewModel
import com.opn.eshopify.presentation.store.StoreDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::StoreDetailViewModel)
    viewModelOf(::OrderSummaryViewModel)
}
