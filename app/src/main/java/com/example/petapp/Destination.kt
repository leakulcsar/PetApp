package com.example.petapp

sealed class Destination(val route: String) {
    data object HomeScreen: Destination("home_screen")
    data class DetailScreen(val animalId: Int): Destination("detail_screen/$animalId") {
        companion object {
            const val ROUTE_DEFINITION = "detail_screen/{animalId}"
        }
    }
}