package com.example.petapp.feature.detail

import com.example.petapp.domain.model.Animal

sealed interface DetailUiState {
    data object Loading: DetailUiState
    data class Loaded(val animal: Animal): DetailUiState
    data class Error(val message: String): DetailUiState
}