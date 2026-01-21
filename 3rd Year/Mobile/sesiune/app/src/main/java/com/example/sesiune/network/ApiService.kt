package com.example.sesiune.network

import com.example.sesiune.data.AuditRequest
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/audit")
    suspend fun submitAudit(@Body request: AuditRequest): Response<Unit>
}

object RetrofitClient {
    private const val BASE_URL = "http://10.231.147.18:3000/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

