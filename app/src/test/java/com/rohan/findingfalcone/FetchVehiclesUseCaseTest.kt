package com.rohan.findingfalcone

import com.rohan.findingfalcone.network.Result
import com.rohan.findingfalcone.repositories.GeekTrustApiRepository
import com.rohan.findingfalcone.usecases.FetchPlanetsUseCase
import com.rohan.findingfalcone.usecases.FetchVehiclesUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class FetchVehiclesUseCaseTest {

    private lateinit var fetchVehiclesUseCase: FetchVehiclesUseCase

    @MockK
    lateinit var geekTrustApiRepository: GeekTrustApiRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        fetchVehiclesUseCase = FetchVehiclesUseCase(geekTrustApiRepository)
    }

    @Test
    fun `test whether invoke function returns appropriate success values`() = runTest {
        coEvery {
            geekTrustApiRepository.fetchVehicles()
        } returns Result.Success(getDummyVehicles())
        val result = fetchVehiclesUseCase.invoke()
        Assert.assertEquals(Result.Success(getDummyVehicles()), result)
    }

    @Test
    fun `test whether invoke function returns appropriate error values`() = runTest {
        coEvery {
            geekTrustApiRepository.fetchVehicles()
        } returns Result.Error(getDummyApiException())
        val result = fetchVehiclesUseCase.invoke()
        Assert.assertEquals(Result.Error(getDummyApiException()), result)
    }
}