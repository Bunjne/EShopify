package com.opn.eshopify.di

import com.opn.eshopify.data.repository.DefaultOrderRepository
import com.opn.eshopify.data.repository.DefaultProductRepository
import com.opn.eshopify.data.repository.DefaultStoreRepository
import com.opn.eshopify.domain.repository.OrderRepository
import com.opn.eshopify.domain.repository.ProductRepository
import com.opn.eshopify.domain.repository.StoreRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::DefaultStoreRepository) { bind<StoreRepository>() }
    singleOf(::DefaultProductRepository) { bind<ProductRepository>() }
    singleOf(::DefaultOrderRepository) { bind<OrderRepository>() }
}
