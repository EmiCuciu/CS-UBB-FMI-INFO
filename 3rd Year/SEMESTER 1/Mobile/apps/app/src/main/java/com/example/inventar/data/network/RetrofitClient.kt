package com.example.inventar.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // Pentru EMULATOR Android folosește: 10.0.2.2
    // Pentru DEVICE FIZIC folosește IP-ul calculatorului din aceeași rețea
    // Pentru USB TETHERING folosește IP-ul calculatorului din subnet-ul tethering

    // private const val BASE_URL = "http://10.0.2.2:3000/" // Android emulator localhost
    // private const val BASE_URL = "http://10.99.138.25:3000/" // Wi-Fi - pentru device fizic
    private const val BASE_URL = "http://10.231.147.116:3000/" // Ethernet 2 / USB Tethering - pentru device fizic
    // private const val BASE_URL = "http://10.231.147.206:3000/" // Gateway Ethernet 2 - pentru USB tethering phone

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: InventoryApiService = retrofit.create(InventoryApiService::class.java)
}

