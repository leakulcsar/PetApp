package com.example.petapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.petapp.feature.detail.DetailScreen
import com.example.petapp.feature.home.HomeScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PetApp() {
    val navController = rememberNavController()

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = Destination.HomeScreen.route
    ) {
        composable(Destination.HomeScreen.route) {
            HomeScreen(
                viewModel = koinViewModel(),
                onNavigateToAnimalDetail = { animalId ->
                    navController.navigate(Destination.DetailScreen(animalId = animalId).route)
                }
            )
        }

        composable(
            route = Destination.DetailScreen.ROUTE_DEFINITION,
            arguments = listOf(navArgument("animalId") { type = NavType.IntType })
        ) {
            val animalId = it.arguments?.getInt("animalId")
                ?: error("Can't open Detail without a valid animalId")
            DetailScreen(
                detailViewModel = koinViewModel(
                    viewModelStoreOwner = it,
                    parameters = { parametersOf(animalId) }),
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}