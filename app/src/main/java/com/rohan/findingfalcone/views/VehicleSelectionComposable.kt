package com.rohan.findingfalcone.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rohan.findingfalcone.R
import com.rohan.findingfalcone.models.Planet
import com.rohan.findingfalcone.models.Vehicle
import com.rohan.findingfalcone.viewmodels.HomeViewModel

/**
 * Layout for select vehicle bottom sheet, handles:
 * - Displaying available for the given planet
 * - Allowing single selection of vehicles
 * - Allowing to select vehicle for a planet
 */
@Composable
fun VehicleSelectionComposable(
    viewModel: HomeViewModel = hiltViewModel(),
    planet: Planet,
    bottomSheetOpenCount: Int,
    onButtonClick: () -> Unit
) {
    val vehicles by viewModel.getAvailableVehiclesForPlanet(planet).collectAsState()

    val selectedVehicles by viewModel.selectedVehicleForPlanets.collectAsState()

    // Local state of the selected vehicles, to be maintained for ensuring single selection
    var selectedVehicle: Vehicle? by remember { mutableStateOf(null) }

    val vehicleCounts by viewModel.vehicleCounts.collectAsState()

    // Added this block for refreshing the state of bottom sheet
    // whenever the bottom sheet is expanded
    LaunchedEffect(bottomSheetOpenCount) {
        selectedVehicle = selectedVehicles[planet]
    }

    Column(Modifier.fillMaxWidth()) {

        // Heading
        Text(
            text = stringResource(id = R.string.select_a_vehicle),
            style = MaterialTheme.typography.h6,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        LazyColumn {
            items(vehicles) { vehicle ->
                // Layout for single list item
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedVehicle = vehicle },
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Text(
                            text = vehicle.name,
                            Modifier.padding(start = 12.dp),
                            style = MaterialTheme.typography.body1,
                            color = Color.Black
                        )
                        Text(
                            text = "Count: ${vehicleCounts[vehicle.name] ?: 0} Max Distance: ${vehicle.maxDistance} Speed: ${vehicle.speed}",
                            Modifier.padding(start = 12.dp),
                            style = MaterialTheme.typography.caption,
                            color = Color.DarkGray
                        )
                    }
                    RadioButton(
                        selected = selectedVehicle?.name == vehicle.name,
                        onClick = { selectedVehicle = vehicle },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
        }
        // Submit button for final selection of vehicle
        Button(
            onClick = {
                selectedVehicle?.let {
                    viewModel.selectVehicle(planet, it)
                }
                onButtonClick.invoke()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp),
            enabled = selectedVehicle != null,
            shape = MaterialTheme.shapes.large
        ) {
            Text(text = stringResource(id = R.string.select_vehicle))
        }
    }
}