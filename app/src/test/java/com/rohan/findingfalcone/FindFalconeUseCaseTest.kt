package com.rohan.findingfalcone

import com.rohan.findingfalcone.network.Result
import com.rohan.findingfalcone.repositories.GeekTrustApiRepository
import com.rohan.findingfalcone.usecases.FetchTokenUseCase
import com.rohan.findingfalcone.usecases.FindFalconeUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FindFalconeUseCaseTest {

    private lateinit var findFalconeUseCase: FindFalconeUseCase

    @MockK
    lateinit var geekTrustApiRepository: GeekTrustApiRepository

    @MockK
    lateinit var tokenUseCase: FetchTokenUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        findFalconeUseCase = FindFalconeUseCase(
            geekTrustApiRepository,
            tokenUseCase
        )
    }

    @Test
    fun `test whether invoke function returns appropriate success values`() = runTest {
        val planets = getDummyPlanets().map { it.name }
        val vehicles = getDummyVehicles().map { it.name }
        coEvery {
            tokenUseCase.invoke()
        } returns Result.Success(mockToken)
        coEvery {
            geekTrustApiRepository.findFalcone(mockToken.token, planets, vehicles)
        } returns Result.Success(mockFindFalconeSuccessResponse())
        val result = findFalconeUseCase.invoke(getDummyPlanets().map { it.name },
            getDummyVehicles().map { it.name })
        advanceUntilIdle()
        Assert.assertEquals(Result.Success(mockFindFalconeSuccessResponse()), result)
    }

    @Test
    fun `test whether token fetch error returns appropriate error values`() = runTest {
        val planets = getDummyPlanets().map { it.name }
        val vehicles = getDummyVehicles().map { it.name }
        coEvery {
            tokenUseCase.invoke()
        } returns Result.Error(getDummyApiException())
        coEvery {
            geekTrustApiRepository.findFalcone(mockToken.token, planets, vehicles)
        } returns Result.Success(mockFindFalconeSuccessResponse())
        val result = findFalconeUseCase.invoke(getDummyPlanets().map { it.name },
            getDummyVehicles().map { it.name })
        advanceUntilIdle()
        Assert.assertEquals(Result.Error(getDummyApiException()), result)
    }

    @Test
    fun `test whether invoke function returns appropriate failure response`() = runTest {
        val planets = getDummyPlanets().map { it.name }
        val vehicles = getDummyVehicles().map { it.name }
        coEvery {
            tokenUseCase.invoke()
        } returns Result.Success(mockToken)
        coEvery {
            geekTrustApiRepository.findFalcone(mockToken.token, planets, vehicles)
        } returns Result.Success(mockFindFalconeFailureResponse())
        val result = findFalconeUseCase.invoke(getDummyPlanets().map { it.name },
            getDummyVehicles().map { it.name })
        advanceUntilIdle()
        Assert.assertEquals(Result.Success(mockFindFalconeFailureResponse()), result)
    }

    @Test
    fun `test whether invoke function returns appropriate error response`() = runTest {
        val planets = getDummyPlanets().map { it.name }
        val vehicles = getDummyVehicles().map { it.name }
        coEvery {
            tokenUseCase.invoke()
        } returns Result.Success(mockToken)
        coEvery {
            geekTrustApiRepository.findFalcone(mockToken.token, planets, vehicles)
        } returns Result.Error(getDummyApiException())
        val result = findFalconeUseCase.invoke(getDummyPlanets().map { it.name },
            getDummyVehicles().map { it.name })
        advanceUntilIdle()
        Assert.assertEquals(Result.Error(getDummyApiException()), result)
    }

}