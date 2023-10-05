package com.rohan.findingfalcone.usecases

import com.rohan.findingfalcone.models.FindFalconeResponse
import com.rohan.findingfalcone.network.Result
import com.rohan.findingfalcone.repositories.GeekTrustApiRepository
import javax.inject.Inject

class FindFalconeUseCase @Inject constructor(
    private val geekTrustApiRepository: GeekTrustApiRepository,
    private val fetchTokenUseCase: FetchTokenUseCase
) {

    suspend operator fun invoke(planets: List<String>, vehicles: List<String>): Result<FindFalconeResponse> {
        return when(val result = fetchTokenUseCase()) {
            is Result.Success -> {
                geekTrustApiRepository.findFalcone(result.data.token, planets, vehicles)
            }
            is Result.Error -> {
                result
            }
        }
    }

}