package com.example.petapp.viewmodel

import com.example.petapp.FakePetRepository
import com.example.petapp.domain.usecase.GetPetsUseCase
import com.example.petapp.feature.home.HomeUiState
import com.example.petapp.feature.home.HomeViewModel
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
class HomeViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val repository = FakePetRepository()
    private val useCase = GetPetsUseCase(repository)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `when view model is initialized then state is Loading`() = runTest {
        val viewModel = HomeViewModel(useCase)
        assertEquals(
            expected = HomeUiState.Loading,
            actual = viewModel.homeUiState.value
        )
    }

    @Test
    fun `given useCase returns list of animals then state is Loaded with expected data`() =
        runTest {
            val expectedAnimal = sampleAnimal()
            repository.animalsResult = Result.success(listOf(expectedAnimal))
            val viewModel = HomeViewModel(useCase)
            runCurrent()
            assertEquals(
                expected = HomeUiState.Loaded(listOf(expectedAnimal)),
                actual = viewModel.homeUiState.value
            )
        }

    @Test
    fun `given useCase returns error then state is Error`() = runTest {
        repository.animalsResult = Result.failure(Exception("Test exception"))
        val viewModel = HomeViewModel(useCase)
        runCurrent()
        assertEquals(
            expected = HomeUiState.Error(message = "Test exception"),
            actual = viewModel.homeUiState.value
        )
    }

}