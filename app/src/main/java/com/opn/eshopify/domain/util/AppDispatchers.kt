package com.opn.eshopify.domain.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface AppDispatchers {
    fun getIODispatcher(): CoroutineDispatcher
    fun getDefaultDispatcher(): CoroutineDispatcher
    fun getMainDispatcher(): CoroutineDispatcher
}

class AppDispatchersImpl : AppDispatchers {
    override fun getIODispatcher() = Dispatchers.IO
    override fun getDefaultDispatcher() = Dispatchers.Default
    override fun getMainDispatcher() = Dispatchers.Main
}
