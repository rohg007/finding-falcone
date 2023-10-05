package com.rohan.findingfalcone.repositories

import com.rohan.findingfalcone.models.FindFalconeRequest
import com.rohan.findingfalcone.models.FindFalconeResponse
import com.rohan.findingfalcone.models.Planet
import com.rohan.findingfalcone.models.Token
import com.rohan.findingfalcone.models.Vehicle
import com.rohan.findingfalcone.network.GeekTrustApiService
import com.rohan.findingfalcone.network.Result
import com.rohan.findingfalcone.network.handleApiResponse
import dagger.Lazy
import javax.inject.Inject

class GeekTrustApiRepository @Inject constructor(
    private val geekTrustApiServiceLazy: Lazy<GeekTrustApiService>
) {
    suspend fun fetchPlanets(): Result<List<Planet>> =
        handleApiResponse(geekTrustApiServiceLazy.get().getPlanets())

    suspend fun fetchVehicles(): Result<List<Vehicle>> =
        handleApiResponse(geekTrustApiServiceLazy.get().getVehicles())

    suspend fun fetchToken(): Result<Token> =
        handleApiResponse(geekTrustApiServiceLazy.get().getToken())

    suspend fun findFalcone(token: String, planets: List<String>, vehicles: List<String>): Result<FindFalconeResponse> =
        handleApiResponse(geekTrustApiServiceLazy.get().findFalcone(FindFalconeRequest( token, planets, vehicles)))
}