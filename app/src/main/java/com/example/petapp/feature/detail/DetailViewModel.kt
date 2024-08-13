package com.example.petapp.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petapp.domain.usecase.GetPetByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val animalId: Int,
    private val getPetByIdUseCase: GetPetByIdUseCase
) : ViewModel() {

    private val _detailUiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val detailUiState = _detailUiState.asStateFlow()

    init {
        getAnimalById()
    }

    fun getAnimalById() {
        viewModelScope.launch {
            getPetByIdUseCase(animalId).onSuccess { animal ->
                _detailUiState.update {
                    DetailUiState.Loaded(animal)
                }

            }.onFailure { throwable ->
                _detailUiState.update {
                    DetailUiState.Error(
                        message = throwable.localizedMessage
                            ?: "Error loading detail of the selected pet."
                    )
                }
            }
        }
    }
}