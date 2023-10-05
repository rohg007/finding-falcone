package com.rohan.findingfalcone.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rohan.findingfalcone.R
import com.rohan.findingfalcone.viewmodels.HomeViewModel

/**
 * Result screen for displaying the final Find Falcone challenge and the time taken
 */
@Composable
fun ResultComposable(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController
) {
    val findFalconeResult by viewModel.findFalconeResult.collectAsState()
    val timeTaken by viewModel.timeTaken.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
        ) {
            // Find falcone result
            Text(
                text = findFalconeResult,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5
            )

            // Time taken
            Text(
                text = stringResource(id = R.string.time_taken_with_value, timeTaken),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6
            )

            // Reset state
            Button(
                onClick = {
                    viewModel.reset()
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            ) {
                Text(text = stringResource(id = R.string.reset))
            }
        }
    }
}