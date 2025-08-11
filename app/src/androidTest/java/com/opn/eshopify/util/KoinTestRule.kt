package com.opn.eshopify.util

import androidx.test.platform.app.InstrumentationRegistry
import com.opn.eshopify.di.apiModule
import com.opn.eshopify.di.appModule
import com.opn.eshopify.di.useCaseModule
import com.opn.eshopify.di.viewModelModule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.module.Module

class KoinTestRule(
    private val modules: List<Module>
) : TestWatcher() {
    override fun starting(description: Description) {

        if (GlobalContext.getKoinApplicationOrNull() == null) {
            startKoin {
                androidContext(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext)
                modules(
                    listOf(
                        appModule,
                        apiModule,
                        useCaseModule,
                        viewModelModule
                    ) + modules.toList()
                )
            }
        } else {
            loadKoinModules(modules)
        }
    }

    override fun finished(description: Description) {
        GlobalContext.unloadKoinModules(modules)
    }
}
