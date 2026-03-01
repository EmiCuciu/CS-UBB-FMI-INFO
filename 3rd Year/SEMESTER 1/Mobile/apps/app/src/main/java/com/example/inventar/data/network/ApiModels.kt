package com.example.inventar.data.network

import com.example.inventar.data.model.Product

data class ProductPageResponse(
    val total: Int,
    val page: Int,
    val products: List<Product>
)

data class ItemRequest(
    val code: Int,
    val quantity: Int
)

data class ItemResponse(
    val success: Boolean,
    val code: Int,
    val quantity: Int
)


