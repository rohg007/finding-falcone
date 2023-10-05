package com.rohan.findingfalcone

import android.util.Log
import com.rohan.findingfalcone.models.Planet
import com.rohan.findingfalcone.models.Vehicle
import com.rohan.findingfalcone.network.Result
import com.rohan.findingfalcone.usecases.FetchPlanetsUseCase
import com.rohan.findingfalcone.usecases.FetchTokenUseCase
import com.rohan.findingfalcone.usecases.FetchVehiclesUseCase
import com.rohan.findingfalcone.usecases.FindFalconeUseCase
import com.rohan.findingfalcone.viewmodels.HomeViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var viewModel: HomeViewModel

    @MockK
    lateinit var fetchPlanetsUseCase: FetchPlanetsUseCase

    @MockK
    lateinit var fetchVehiclesUseCase: FetchVehiclesUseCase

    @MockK
    lateinit var findFalconeUseCase: FindFalconeUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this)
        coEvery {
            fetchPlanetsUseCase.invoke()
        } returns Result.Success(getDummyPlanets())
        coEvery {
            fetchVehiclesUseCase.invoke()
        } returns Result.Success(getDummyVehicles())
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } answers {
            println(arg<String>(1))
            0
        }
        viewModel = HomeViewModel(
            fetchPlanetsUseCase,
            fetchVehiclesUseCase,
            findFalconeUseCase
        )
    }

    @Test
    fun `test whether planets state is updated on init`() = runTest {
        advanceUntilIdle()
        Assert.assertEquals(getDummyPlanets(), viewModel.planets.value)
    }

    @Test
    fun `test whether select vehicle updates the state in selectedVehicleForPlanets`() = runTest {
        viewModel.selectVehicle(getDummyPlanets().first(), getDummyVehicles().first())
        val expected = mapOf(Pair(getDummyPlanets().first(), getDummyVehicles().first()))
        Assert.assertEquals(expected, viewModel.selectedVehicleForPlanets.value)
    }

    @Test
    fun `test whether select vehicle replaces the previous selection in selectedVehicleForPlanets`() = runTest {
        viewModel.selectVehicle(getDummyPlanets().first(), getDummyVehicles().first())
        viewModel.selectVehicle(getDummyPlanets().first(), getDummyVehicles()[1])
        val expected = mapOf(Pair(getDummyPlanets().first(), getDummyVehicles()[1]))
        Assert.assertEquals(expected, viewModel.selectedVehicleForPlanets.value)
    }

    @Test
    fun `test whether select vehicle updates the time taken`() = runTest {
        viewModel.selectVehicle(getDummyPlanets().first(), getDummyVehicles().first())
        Assert.assertEquals(50L, viewModel.timeTaken.value)
    }

    @Test
    fun `test whether select vehicle updates the vehicle count state by 1`() = runTest {
        viewModel.selectVehicle(getDummyPlanets().first(), getDummyVehicles().first())
        Assert.assertEquals(0, viewModel.vehicleCounts.value[getDummyVehicles().first().name])
    }

    @Test
    fun `test whether remove vehicle updates the state in selectedVehicleForPlanets`() = runTest {
        viewModel.selectVehicle(getDummyPlanets().first(), getDummyVehicles().first())
        viewModel.removeVehicle(getDummyPlanets().first())
        val expected = emptyMap<Planet, Vehicle>()
        Assert.assertEquals(expected, viewModel.selectedVehicleForPlanets.value)
    }

    @Test
    fun `test whether remove vehicle updates the time taken`() = runTest {
        viewModel.selectVehicle(getDummyPlanets().first(), getDummyVehicles().first())
        viewModel.removeVehicle(getDummyPlanets().first())
        Assert.assertEquals(0L, viewModel.timeTaken.value)
    }

    @Test
    fun `test whether remove vehicle updates the vehicle count state by 1`() = runTest {
        viewModel.selectVehicle(getDummyPlanets().first(), getDummyVehicles().first())
        viewModel.removeVehicle(getDummyPlanets().first())
        Assert.assertEquals(1, viewModel.vehicleCounts.value[getDummyVehicles().first().name])
    }

    @Test
    fun `test whether get available vehicles for planet returns correct filtered list`() = runTest {
        val result = viewModel.getAvailableVehiclesForPlanet(getDummyPlanets()[1])
        Assert.assertEquals(listOf(getDummyVehicles().first()), result.value)
    }

    @Test
    fun `test whether reset function resets all states of view model`() = runTest {
        viewModel.reset()
        Assert.assertEquals(emptyMap<Planet, Vehicle>(), viewModel.selectedVehicleForPlanets.value)
        Assert.assertEquals(emptyMap<String, Int>(), viewModel.vehicleCounts.value)
        Assert.assertEquals("", viewModel.findFalconeResult.value)
        Assert.assertEquals(0, viewModel.timeTaken.value)
    }
}