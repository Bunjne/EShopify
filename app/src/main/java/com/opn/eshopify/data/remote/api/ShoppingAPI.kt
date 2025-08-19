package com.opn.eshopify.data.remote.api

import com.opn.eshopify.data.remote.model.OrderDto
import com.opn.eshopify.data.remote.model.ProductResponse
import com.opn.eshopify.data.remote.model.StoreDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ShoppingAPI {

    @GET("/products")
    suspend fun getProducts(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): ProductResponse

    @GET("/storeinfo")
    suspend fun getStoreDetail(): StoreDto

    @POST("/order")
    suspend fun makeOrder(@Body order: OrderDto)
}
