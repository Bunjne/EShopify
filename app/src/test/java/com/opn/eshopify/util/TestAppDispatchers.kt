package com.opn.eshopify.util

import com.opn.eshopify.domain.util.AppDispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@ExperimentalCoroutinesApi
class TestAppDispatchers(
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : AppDispatchers {
    override fun getIODispatcher() = dispatcher

    override fun getDefaultDispatcher() = dispatcher

    override fun getMainDispatcher() = dispatcher
}
