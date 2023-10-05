package com.rohan.findingfalcone

import com.rohan.findingfalcone.network.Result
import com.rohan.findingfalcone.repositories.GeekTrustApiRepository
import com.rohan.findingfalcone.usecases.FetchPlanetsUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class FetchPlanetsUseCaseTest {

    private lateinit var fetchPlanetsUseCase: FetchPlanetsUseCase

    @MockK
    lateinit var geekTrustApiRepository: GeekTrustApiRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        fetchPlanetsUseCase = FetchPlanetsUseCase(geekTrustApiRepository)
    }

    @Test
    fun `test whether invoke function returns appropriate success values`() = runTest {
        coEvery {
            geekTrustApiRepository.fetchPlanets()
        } returns Result.Success(getDummyPlanets())
        val result = fetchPlanetsUseCase.invoke()
        Assert.assertEquals(Result.Success(getDummyPlanets()), result)
    }

    @Test
    fun `test whether invoke function returns appropriate error values`() = runTest {
        coEvery {
            geekTrustApiRepository.fetchPlanets()
        } returns Result.Error(getDummyApiException())
        val result = fetchPlanetsUseCase.invoke()
        Assert.assertEquals(Result.Error(getDummyApiException()), result)
    }
}