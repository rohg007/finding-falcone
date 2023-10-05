package com.rohan.findingfalcone.usecases

import com.rohan.findingfalcone.models.Vehicle
import com.rohan.findingfalcone.network.Result
import com.rohan.findingfalcone.repositories.GeekTrustApiRepository
import javax.inject.Inject

class FetchVehiclesUseCase @Inject constructor(
    private val repository: GeekTrustApiRepository
) {
    suspend operator fun invoke(): Result<List<Vehicle>> = repository.fetchVehicles()
}