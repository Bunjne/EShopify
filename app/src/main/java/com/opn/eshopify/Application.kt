package com.opn.eshopify

import android.app.Application
import com.opn.eshopify.di.apiModule
import com.opn.eshopify.di.appModule
import com.opn.eshopify.di.repositoryModule
import com.opn.eshopify.di.useCaseModule
import com.opn.eshopify.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@Application)
            modules(
                listOf(
                    appModule,
                    apiModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}
