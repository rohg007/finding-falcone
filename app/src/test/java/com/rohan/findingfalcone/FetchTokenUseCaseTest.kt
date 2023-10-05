package com.rohan.findingfalcone

import com.rohan.findingfalcone.network.Result
import com.rohan.findingfalcone.repositories.GeekTrustApiRepository
import com.rohan.findingfalcone.usecases.FetchTokenUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class FetchTokenUseCaseTest {

    private lateinit var tokenUseCase: FetchTokenUseCase

    @MockK
    lateinit var geekTrustApiRepository: GeekTrustApiRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        tokenUseCase = FetchTokenUseCase(geekTrustApiRepository)
    }

    @Test
    fun `test whether invoke function returns appropriate success values`() = runTest {
        coEvery {
            geekTrustApiRepository.fetchToken()
        } returns Result.Success(mockToken)
        val result = tokenUseCase.invoke()
        Assert.assertEquals(Result.Success(mockToken), result)
    }

    @Test
    fun `test whether invoke function returns appropriate error values`() = runTest {
        coEvery {
            geekTrustApiRepository.fetchToken()
        } returns Result.Error(getDummyApiException())
        val result = tokenUseCase.invoke()
        Assert.assertEquals(Result.Error(getDummyApiException()), result)
    }
}