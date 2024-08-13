package com.example.petapp.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petapp.domain.usecase.GetPetsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getPetsUseCase: GetPetsUseCase
) : ViewModel() {

    private val _homeUiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val homeUiState = _homeUiState.asStateFlow()

    init {
        getAnimals()
    }

    fun getAnimals() {
        viewModelScope.launch {
            getPetsUseCase().onSuccess { animals ->
                _homeUiState.update {
                    HomeUiState.Loaded(animalList = animals)
                }
            }.onFailure { throwable ->
                _homeUiState.update {
                    HomeUiState.Error(
                        message = throwable.localizedMessage
                            ?: "Failed to load the pet list. Check your connectivity and try again."
                    )
                }
            }
        }
    }

}