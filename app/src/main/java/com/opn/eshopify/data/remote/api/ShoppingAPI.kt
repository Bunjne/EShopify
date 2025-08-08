package com.opn.eshopify.data.remote.api

import com.opn.eshopify.data.remote.model.OrderDto
import com.opn.eshopify.data.remote.model.ProductDto
import com.opn.eshopify.data.remote.model.StoreDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ShoppingAPI {

    @GET("/products")
    suspend fun getProducts(): List<ProductDto>

    @GET("/storeinfo")
    suspend fun getStoreDetail(): StoreDto

    @POST("/orders")
    suspend fun makeOrder(@Body order: OrderDto)
}
