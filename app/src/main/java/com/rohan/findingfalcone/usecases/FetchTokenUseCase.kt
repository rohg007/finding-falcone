package com.rohan.findingfalcone.usecases

import com.rohan.findingfalcone.models.Token
import com.rohan.findingfalcone.network.Result
import com.rohan.findingfalcone.repositories.GeekTrustApiRepository
import javax.inject.Inject

class FetchTokenUseCase @Inject constructor(
    private val geekTrustApiRepository: GeekTrustApiRepository
) {
    suspend operator fun invoke(): Result<Token> = geekTrustApiRepository.fetchToken()
}