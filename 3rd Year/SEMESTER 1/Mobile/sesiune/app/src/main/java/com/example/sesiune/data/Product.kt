package com.example.sesiune.data

data class Product(
    val code: Int,
    val name: String,
    val quantity: Int,
    var counted: Int? = null,
    var isSubmitted: Boolean = false,
    var hasError: Boolean = false,
    var isSubmitting: Boolean = false
)
