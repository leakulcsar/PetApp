package com.example.petapp.usecase

import com.example.petapp.FakePetRepository
import com.example.petapp.domain.usecase.GetPetsUseCase
import com.example.petapp.sampleAnimal
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

internal class GetPetsUseCaseTest {
    private val petRepository = FakePetRepository()
    private val useCase = GetPetsUseCase(petRepository)


    @Test
    fun `use case returns expected result when success`() = runBlocking {
        val result = useCase()
        assertEquals(
            expected = Result.success(listOf(sampleAnimal())),
            actual = result
        )
    }

    @Test
    fun `use case returns error when data source fails`() = runBlocking {
        val expectedException = Exception("Data source failed")
        petRepository.animalsResult = Result.failure(expectedException)
        val result = useCase()
        assertEquals(
            expected = Result.failure(expectedException),
            actual = result
        )
    }
}