package com.rohan.findingfalcone

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rohan.findingfalcone.models.Planet
import com.rohan.findingfalcone.navigation.Routes
import com.rohan.findingfalcone.ui.theme.FindingFalconeTheme
import com.rohan.findingfalcone.viewmodels.HomeViewModel
import com.rohan.findingfalcone.views.HomeComposable
import com.rohan.findingfalcone.views.ResultComposable
import com.rohan.findingfalcone.views.VehicleSelectionComposable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Entry point to all screens in the app
 * Using single activity along with compose navigation to keep the app lightweight
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Injecting view model in activity itself as this is a shared view model across all composable
    private val homeViewModel by viewModels<HomeViewModel>()

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initiate observing of live data here
        observeViewModelData()
        // navigation setup follows here
        // entry point to App's UI
        setContent {
            val navController = rememberNavController()
            // Modal bottom sheet state to switch between the hidden/expanded states seamlessly
            val modalSheetState = rememberModalBottomSheetState(
                initialValue = ModalBottomSheetValue.Hidden,
                confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
                skipHalfExpanded = true
            )
            // Coroutine scope to launch coroutines for UI intensive tasks such as opening & closing
            // the bottom sheet
            val coroutineScope = rememberCoroutineScope()
            val bottomSheetOpenCount = remember { mutableIntStateOf(0) }
            FindingFalconeTheme {
                val selectedPlanet = remember { mutableStateOf<Planet?>(null) }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Hosts the composable and attaches then to respective routes
                    NavHost(navController = navController, startDestination = Routes.HOME.route) {
                        // Attach HomeComposable to home routes, manages planets, vehicle selection and finding falcone
                        composable(Routes.HOME.route) {
                            ModalBottomSheetLayout(
                                sheetContent = {
                                    selectedPlanet.value?.let {
                                        VehicleSelectionComposable(viewModel = homeViewModel, planet = it, bottomSheetOpenCount.intValue) {
                                            coroutineScope.launch {
                                                modalSheetState.hide()
                                            }
                                        }
                                    }
                                },
                                sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                                sheetState = modalSheetState,
                            ) {
                                HomeComposable(viewModel = homeViewModel, planetsFlow = homeViewModel.planets, navController = navController) { planet ->
                                    coroutineScope.launch {
                                        if (modalSheetState.isVisible)
                                            modalSheetState.hide()
                                        else {
                                            selectedPlanet.value = planet
                                            bottomSheetOpenCount.intValue = bottomSheetOpenCount.intValue + 1
                                            modalSheetState.show()
                                        }
                                    }
                                }
                            }
                        }
                        // Attach ResultComposable to result route for showing the final result
                        composable(Routes.RESULT.route) {
                            ResultComposable(viewModel = homeViewModel, navController = navController)
                        }
                    }
                }
            }
        }
    }

    private fun observeViewModelData() {
        homeViewModel.errorLiveData.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }
}