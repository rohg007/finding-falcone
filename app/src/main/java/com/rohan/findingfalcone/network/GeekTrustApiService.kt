package com.rohan.findingfalcone.network

import com.rohan.findingfalcone.models.FindFalconeRequest
import com.rohan.findingfalcone.models.FindFalconeResponse
import com.rohan.findingfalcone.models.Planet
import com.rohan.findingfalcone.models.Token
import com.rohan.findingfalcone.models.Vehicle
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface GeekTrustApiService {

    @GET("planets")
    suspend fun getPlanets(): Response<List<Planet>>

    @GET("vehicles")
    suspend fun getVehicles(): Response<List<Vehicle>>

    @Headers("Accept: application/json")
    @POST("token")
    suspend fun getToken(): Response<Token>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("find")
    suspend fun findFalcone(@Body findFalconeRequest: FindFalconeRequest): Response<FindFalconeResponse>
}