package com.example.mystery.communication

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface MysteryAPI {
    @Headers("Content-Type: application/json")
    @GET("mystery.json")
    suspend fun getMystery(
    ): Response<MysteryItem>

}