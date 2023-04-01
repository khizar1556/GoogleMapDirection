package com.example.khizar_kotlin.features.direction.data.remote

import com.example.khizar_kotlin.features.direction.domain.model.GooglePlacesInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleDirectionApi {
    @GET("/maps/api/directions/json")
    suspend fun getDirection(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") key: String
    ): GooglePlacesInfo

    companion object {
        const val BASE_URL = "https://maps.googleapis.com/"
    }
}