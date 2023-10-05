package com.rohan.findingfalcone.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rohan.findingfalcone.R
import com.rohan.findingfalcone.models.Planet
import com.rohan.findingfalcone.navigation.Routes
import com.rohan.findingfalcone.viewmodels.HomeViewModel
import kotlinx.coroutines.flow.StateFlow

/**
 * Represents the layout for Home screen, handles:
 * - Displaying the list of planets
 * - Showing the current state of selected vehicles for planets
 * - Hosting an entry point to select vehicles for a planet
 * - Initiating the search for Falcone
 */
@Composable
fun HomeComposable(
    viewModel: HomeViewModel = hiltViewModel(),
    planetsFlow: StateFlow<List<Planet>>,
    navController: NavController,
    onButtonClick: (Planet) -> Unit
) {

    val planets by planetsFlow.collectAsState()
    val timeTaken by viewModel.timeTaken.collectAsState()
    val selectedVehicles by viewModel.selectedVehicleForPlanets.collectAsState()
    val planetsImg = remember { listOf(
        R.drawable.planet_one,
        R.drawable.planet_two,
        R.drawable.planet_three,
        R.drawable.planet_four,
        R.drawable.planet_five,
        R.drawable.planet_six
    ) }

    Column(Modifier.fillMaxHeight()) {
        Column(
            modifier = Modifier
                .weight(1f, true)
        ) {
            Text(
                text = stringResource(id = R.string.please_assign_vehicles),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(id = R.string.time_taken_with_value, timeTaken),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
            // Column grid with 2 rows and 2 columns
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(planets) { index, planet ->
                    PlanetItem(
                        viewModel = viewModel,
                        planet = planet,
                        onButtonClick = { onButtonClick.invoke(planet) },
                        planetImgRes = planetsImg[index%6]
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
        ElevatedButton(
            onClick = {
                viewModel.findFalcone()
                navController.navigate(Routes.RESULT.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            enabled = selectedVehicles.size == 4
        ) {
            Text(text = stringResource(id = R.string.find_falcone))
        }
    }
}


