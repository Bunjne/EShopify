package com.opn.eshopify.di

import com.opn.eshopify.domain.repository.CartRepository
import com.opn.eshopify.domain.repository.OrderRepository
import com.opn.eshopify.domain.repository.ProductRepository
import com.opn.eshopify.domain.repository.StoreRepository
import com.opn.eshopify.mock.repository.FakeCartRepository
import com.opn.eshopify.mock.repository.FakeOrderRepository
import com.opn.eshopify.mock.repository.FakeProductRepository
import com.opn.eshopify.mock.repository.FakeStoreRepository
import org.koin.core.module.Module
import org.koin.dsl.module

val testRepositoryModule: Module = module {
    single<CartRepository> { FakeCartRepository() }
    single<ProductRepository> { FakeProductRepository() }
    single<StoreRepository> { FakeStoreRepository() }
    single<OrderRepository> { FakeOrderRepository() }
}
