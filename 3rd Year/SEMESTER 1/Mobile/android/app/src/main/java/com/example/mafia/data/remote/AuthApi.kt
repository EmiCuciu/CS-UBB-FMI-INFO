package com.example.mafia.data.remote

import com.example.mafia.data.model.LoginRequest
import com.example.mafia.data.model.LoginResponse
import com.example.mafia.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body user: User): Response<LoginResponse>
}

