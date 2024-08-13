package com.example.petapp.feature.home

import com.example.petapp.domain.model.Animal

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Loaded(val animalList: List<Animal>) : HomeUiState

    data class Error(val message: String) : HomeUiState
}
