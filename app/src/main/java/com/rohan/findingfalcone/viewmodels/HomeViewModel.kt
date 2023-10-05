package com.rohan.findingfalcone.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohan.findingfalcone.models.Planet
import com.rohan.findingfalcone.models.Vehicle
import com.rohan.findingfalcone.network.Result
import com.rohan.findingfalcone.usecases.FetchPlanetsUseCase
import com.rohan.findingfalcone.usecases.FetchVehiclesUseCase
import com.rohan.findingfalcone.usecases.FindFalconeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.ArithmeticException
import javax.inject.Inject

/**
 * View-model to handle the business logic associated with Finding Falcone
 * This viewModel is shared between various various screens to keep the UI data/event driven
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchPlanetsUseCase: FetchPlanetsUseCase,
    private val fetchVehiclesUseCase: FetchVehiclesUseCase,
    private val findFalconeUseCase: FindFalconeUseCase
) : ViewModel() {

    /**
     * State holder to hold the list of all planets
     */
    private val _planets = MutableStateFlow<List<Planet>>(emptyList())
    val planets: StateFlow<List<Planet>> = _planets

    /**
     * State holder to hold the list of all vehicles
     */
    private val _vehicles = MutableStateFlow<List<Vehicle>>(emptyList())

    /**
     * Livedata to hold the error string in case of any API errors
     */
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    /**
     * State holder to hold the map of vehicle that is selected for each planet
     */
    private val _selectedVehicleForPlanets = MutableStateFlow(mutableStateMapOf<Planet, Vehicle>())
    val selectedVehicleForPlanets: StateFlow<Map<Planet, Vehicle>> = _selectedVehicleForPlanets

    /**
     * State holder to hold the counts of available vehicles in real-time
     */
    private val _vehicleCounts = MutableStateFlow(mutableStateMapOf<String, Int>())
    val vehicleCounts: StateFlow<Map<String, Int>> = _vehicleCounts

    /**
     * State holder to hold the result of finding falcone with the given planets and vehicles
     */
    private val _findFalconeResult = MutableStateFlow("")
    val findFalconeResult: StateFlow<String> = _findFalconeResult

    /**
     * State holder for the time taken to find falcone
     */
    private val _timeTaken = MutableStateFlow(0L)
    val timeTaken: StateFlow<Long> = _timeTaken

    init {
        fetchPlanets()
        fetchVehicles()
    }

    /**
     * Fetch planets from the network
     */
    private fun fetchPlanets() {
        viewModelScope.launch(Dispatchers.Main) {
            when (val result = fetchPlanetsUseCase()) {
                is Result.Success -> _planets.value = result.data
                is Result.Error -> errorLiveData.value = result.apiException.message
            }
        }
    }

    /**
     * Fetch vehicles from the network
     */
    private fun fetchVehicles() {
        viewModelScope.launch {
            when (val result = fetchVehiclesUseCase()) {
                is Result.Success -> {
                    _vehicles.value = result.data
                    // update the available vehicle counts
                    _vehicles.value.forEach {
                        _vehicleCounts.value[it.name] = it.totalNo
                    }
                }
                is Result.Error -> errorLiveData.value = result.apiException.message
            }
        }
    }

    /**
     * Initiates the API call to the /find endpoint
     * Result of this function is a string that is to be displayed on the result screen
     */
    fun findFalcone() {
        viewModelScope.launch {
            when (val result = findFalconeUseCase(selectedVehicleForPlanets.value.keys.toList().take(4).map { it.name }, selectedVehicleForPlanets.value.values.take(4).map { it.name })) {
                is Result.Success -> result.data.planetName?.let {
                    _findFalconeResult.value = "Success! Queen Falcone is found on planet: $it"
                } ?: result.data.status?.let {
                    _findFalconeResult.value = "Sorry! Queen Falcone could not be found"
                } ?: result.data.error?.let {
                    _findFalconeResult.value = "Uh-oh! We faced a server error"
                }
                is Result.Error -> errorLiveData.value = result.apiException.message
            }
        }
    }

    /**
     * Selects vehicle for the given planet
     * @param planet: The planet for which planet is to be selected
     * @param vehicle: The vehicle that is selected for the vehicle
     */
    fun selectVehicle(planet: Planet, vehicle: Vehicle) {
        // release the existing selected vehicle for the given planet
        removeVehicle(planet)
        _selectedVehicleForPlanets.value[planet] = vehicle
        // update the count of available vehicles
        _vehicleCounts.value.merge(vehicle.name, 1, Int::minus)
        updateTimeTaken()
    }

    /**
     * Removes the selected vehicle for the given planet
     * @param planet: The planet for which to remove the selected vehicle
     * Since it is a single selection, we entirely remove the entry from the map
     */
    fun removeVehicle(planet: Planet) {
        // update vehicle counts for available vehicles
        _selectedVehicleForPlanets.value[planet]?.let {
            _vehicleCounts.value.merge(it.name, 1, Int::plus)
        }
        _selectedVehicleForPlanets.value.remove(planet)
        updateTimeTaken()
    }

    /**
     * @return list of eligible vehicles for the given planet as a state flow
     * @param planet: The planet for which eligible vehicles need to be returned
     * This function filters the vehicles from all vehicles with the given criteria:
     * - The remaining vehicle count for the vehicle should be >0
     * - The max distance that can be covered by vehicle should be >= planet's distance
     */
    fun getAvailableVehiclesForPlanet(planet: Planet): StateFlow<List<Vehicle>> =
        MutableStateFlow(_vehicles.value.filter { it.maxDistance >= planet.distance && _vehicleCounts.value.getOrDefault(it.name, 0) > 0 }).asStateFlow()

    /**
     * Updates the time taken for the search with the given set of planets and vehicles
     */
    private fun updateTimeTaken() {
        try {
            var maxTime = 0L
            _selectedVehicleForPlanets.value.forEach { (planet, vehicle) ->
                maxTime = maxOf(maxTime, planet.distance/vehicle.speed)
            }
            _timeTaken.value = maxTime
        } catch (e: ArithmeticException) {
            Log.e("abc", e.toString())
        }
    }

    /**
     * Resets the state of homeViewModel to reset the UI state
     */
    fun reset() {
        fetchPlanets()
        fetchVehicles()
        _selectedVehicleForPlanets.value.clear()
        _vehicleCounts.value.clear()
        _findFalconeResult.value = ""
        _timeTaken.value = 0
    }
}