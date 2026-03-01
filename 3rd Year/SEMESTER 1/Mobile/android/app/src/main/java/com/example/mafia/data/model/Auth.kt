package com.example.mafia.data.model

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String
)

data class User(
    val username: String,
    val password: String
)

