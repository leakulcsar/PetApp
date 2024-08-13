package com.example.petapp.viewmodel

import com.example.petapp.FakePetRepository
import com.example.petapp.domain.usecase.GetPetByIdUseCase
import com.example.petapp.feature.detail.DetailUiState
import com.example.petapp.feature.detail.DetailViewModel
import com.example.petapp.sampleAnimal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {
    private val testDisposable = StandardTestDispatcher()
    private val repository = FakePetRepository()
    private val useCase = GetPetByIdUseCase(repository)

    @Before
    fun setup() {
        Dispatchers.setMain(testDisposable)
    }

    @Test
    fun `given useCase returns animal with the given id, then state is Loaded with expected data`() =
        runTest {
            val viewModel = DetailViewModel(animalId = 123, useCase)
            runCurrent()
            assertEquals(
                expected = DetailUiState.Loaded(sampleAnimal()),
                actual = viewModel.detailUiState.value
            )
        }

    @Test
    fun `given useCase returns error then state is Error`() = runTest {
        repository.animalByIdResult = Result.failure(Exception("Test exception"))
        val viewModel = DetailViewModel(animalId = 123, useCase)
        runCurrent()
        assertEquals(
            expected = DetailUiState.Error(message = "Test exception"),
            actual = viewModel.detailUiState.value
        )
    }

}