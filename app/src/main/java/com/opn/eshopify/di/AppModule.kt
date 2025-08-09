package com.opn.eshopify.di

import com.opn.eshopify.domain.util.AppDispatchers
import com.opn.eshopify.domain.util.AppDispatchersImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::AppDispatchersImpl) { bind<AppDispatchers>() }
}
