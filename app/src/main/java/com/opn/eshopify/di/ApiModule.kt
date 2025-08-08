package com.opn.eshopify.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.opn.eshopify.BuildConfig
import com.opn.eshopify.data.extension.okHttpClient
import com.opn.eshopify.data.extension.retrofit
import com.opn.eshopify.data.remote.api.ShoppingAPI
import java.util.concurrent.TimeUnit
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.dsl.module
import retrofit2.Retrofit

const val API_TIMEOUT = 60L

val apiModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
        }
    }

    single {
        retrofit {
            baseUrl(BuildConfig.API_URL)
            okHttpClient {
                connectTimeout(API_TIMEOUT, TimeUnit.SECONDS)
                readTimeout(API_TIMEOUT, TimeUnit.SECONDS)
                writeTimeout(API_TIMEOUT, TimeUnit.SECONDS)
            }
            addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaType()))
        }
    }

    single {
        get<Retrofit>().create(ShoppingAPI::class.java)
    }
}
