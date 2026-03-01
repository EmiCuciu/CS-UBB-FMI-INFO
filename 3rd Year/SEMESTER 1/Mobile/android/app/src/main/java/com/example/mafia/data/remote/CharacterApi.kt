package com.example.mafia.data.remote

import com.example.mafia.data.model.Character
import retrofit2.Response
import retrofit2.http.*

interface CharacterApi {

    @GET("characters")
    suspend fun getAllCharacters(): Response<List<Character>>

    @GET("characters/{id}")
    suspend fun getCharacterById(@Path("id") id: String): Response<Character>

    @POST("characters")
    suspend fun createCharacter(@Body character: Character): Response<Character>

    @PUT("characters/{id}")
    suspend fun updateCharacter(
        @Path("id") id: String,
        @Body character: Character
    ): Response<Character>

    @DELETE("characters/{id}")
    suspend fun deleteCharacter(@Path("id") id: String): Response<Unit>
}

