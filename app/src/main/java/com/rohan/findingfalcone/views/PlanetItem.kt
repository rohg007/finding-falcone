package com.rohan.findingfalcone.views

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rohan.findingfalcone.R
import com.rohan.findingfalcone.models.Planet
import com.rohan.findingfalcone.viewmodels.HomeViewModel

/**
 * ListItem for a single planet, handles
 * - Planet details
 * - Assigned vehicle for planet
 * - Entry point to assign vehicle
 * - Unassign a vehicle
 */
@Composable
fun PlanetItem(
    viewModel: HomeViewModel = hiltViewModel(),
    planet: Planet,
    onButtonClick: () -> Unit,
    @DrawableRes
    planetImgRes: Int
) {

    val selectedVehicles by viewModel.selectedVehicleForPlanets.collectAsState()

    val isVehicleSelected by remember(planet, selectedVehicles) { derivedStateOf {
        selectedVehicles[planet] != null
    } }

    val borderShape = MaterialTheme.shapes.medium

    Column(
        modifier = Modifier
            .wrapContentSize()
            .border(width = 2.dp, color = Color.Black, shape = borderShape)
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Planet image
        Image(
            painter = painterResource(id = planetImgRes),
            contentDescription = null, // You should provide a description
            modifier = Modifier
                .width(120.dp)
                .height(120.dp)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Planet name
        Text(
            text = planet.name,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterHorizontally),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Planet distance
        Text(
            text = "Distance: ${planet.distance}",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterHorizontally),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Assigned vehicle
        selectedVehicles[planet]?.let {
            Text(
                text = stringResource(id = R.string.assigned_vehicle, it.name),
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall
            )
        }

        // Add vehicle button
        Button(
            onClick = { onButtonClick.invoke() },
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterHorizontally),
            enabled = selectedVehicles[planet] != null || selectedVehicles.size < 4
        ) {
            Text(
                text = if (isVehicleSelected)
                    stringResource(id = R.string.change_vehicle)
                else
                    stringResource(id = R.string.add_vehicle)
            )
        }

        // Unassign button
        if (isVehicleSelected) {
            TextButton(
                onClick = { viewModel.removeVehicle(planet) },
                modifier = Modifier.wrapContentSize().align(Alignment.CenterHorizontally)
            ) {
                Text(text = stringResource(id = R.string.unassign))
            }
        }
    }
}