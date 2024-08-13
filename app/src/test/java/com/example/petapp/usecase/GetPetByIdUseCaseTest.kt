package com.example.petapp.usecase

import com.example.petapp.FakePetRepository
import com.example.petapp.domain.usecase.GetPetByIdUseCase
import com.example.petapp.sampleAnimal
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

internal class GetPetByIdUseCaseTest {
    private val petRepository = FakePetRepository()
    private val useCase = GetPetByIdUseCase(petRepository)

    @Test
    fun `use case returns expected result when success`() = runBlocking {
        val result = useCase(123)
        assertEquals(
            expected = Result.success(sampleAnimal()),
            actual = result
        )
    }

    @Test
    fun `use case returns error if data source fails`() = runBlocking {
        val expectedException = Exception("Data source failed")
        petRepository.animalByIdResult = Result.failure(expectedException)

        val result = useCase(123)
        assertEquals(
            expected = Result.failure(expectedException),
            actual = result
        )
    }
}