package com.opn.eshopify

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.opn.eshopify.di.apiModule
import com.opn.eshopify.di.appModule
import com.opn.eshopify.di.testRepositoryModule
import com.opn.eshopify.di.useCaseModule
import com.opn.eshopify.di.viewModelModule
import org.koin.core.context.startKoin

class InstrumentationTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        classLoader: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(classLoader, TestApplication::class.java.name, context)
    }
}

class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val productionModule = listOf(
            appModule,
            apiModule,
            useCaseModule,
            viewModelModule
        )
        val testModule = listOf(testRepositoryModule)

        startKoin {
            modules(productionModule + testModule)
        }
    }
}
