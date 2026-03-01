package com.example.inventar.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface InventoryApiService {
    @GET("product")
    suspend fun getProducts(@Query("page") page: Int): Response<ProductPageResponse>

    @POST("item")
    suspend fun postItem(@Body item: ItemRequest): Response<ItemResponse>
}

