package com.example.petapp.repository

import com.example.petapp.data.model.AnimalResponse
import com.example.petapp.data.model.AnimalsResponse
import com.example.petapp.data.model.BreedsResponse
import com.example.petapp.data.model.TokenResponse
import com.example.petapp.data.network.PetService
import com.example.petapp.data.repository.PetRepositoryImpl
import com.example.petapp.domain.model.Animal
import com.example.petapp.domain.model.Breed
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import kotlin.test.Test
import kotlin.test.assertEquals

internal class PetRepositoryImplTest {

    private val fakePetService = FakePetService()

    @Test
    fun `getAnimals returns success with mapped animals from remote data source`() = runTest {
        val petRepository = PetRepositoryImpl(remoteDataSource = fakePetService)
        fakePetService.fakeAnimalsResponse = { sampleAnimalsResponse() }
        val result = petRepository.getAnimals()
        assertEquals(
            expected = Result.success(
                listOf(sampleAnimal(), sampleAnimal())
            ),
            actual = result
        )
    }

    @Test
    fun `getAnimals returns failure if remote data source throws an exception`() = runTest {
        val petRepository = PetRepositoryImpl(remoteDataSource = fakePetService)
        val expectedException = retrofit2.HttpException(
            retrofit2.Response.error<TokenResponse>(
                400,
                "".toResponseBody()
            )
        )
        fakePetService.fakeAnimalsResponse = { throw expectedException }
        val result = petRepository.getAnimals()
        assertEquals(
            expected = Result.failure(expectedException),
            actual = result
        )
    }

    @Test
    fun `getAnimalById returns cached animal if found`() = runTest {
        val petRepository = PetRepositoryImpl(
            remoteDataSource = fakePetService, initialCachedAnimals = listOf(
                sampleAnimal()
            )
        )
        val result = petRepository.getAnimalById(123)
        assertEquals(
            expected = Result.success(
                sampleAnimal()
            ),
            actual = result
        )
    }

    @Test
    fun `getAnimalById fetches animal from remote data source if not cached`() = runTest {
        val petRepository = PetRepositoryImpl(
            remoteDataSource = fakePetService, initialCachedAnimals = listOf(
                sampleAnimal()
            )
        )

        fakePetService.fakeAnimalResponse = { sampleAnimalResponse().copy(id = 111) }
        val result = petRepository.getAnimalById(111)
        assertEquals(
            expected = Result.success(
                sampleAnimal().copy(id = 111)
            ),
            actual = result
        )
    }

    @Test
    fun `fetchAnimalById returns success with mapped animal from remote data source`() = runTest {
        val petRepository = PetRepositoryImpl(remoteDataSource = fakePetService)
        fakePetService.fakeAnimalResponse = { sampleAnimalResponse() }
        val result = petRepository.fetchAnimalById(123)
        assertEquals(
            expected = Result.success(
                sampleAnimal()
            ), actual = result
        )
    }

    @Test
    fun `fetchAnimalById returns failure if remote data source throws an exception`() = runTest {
        val petRepository = PetRepositoryImpl(remoteDataSource = fakePetService)
        val expectedException = retrofit2.HttpException(
            retrofit2.Response.error<AnimalResponse>(
                400,
                "".toResponseBody()
            )
        )
        fakePetService.fakeAnimalResponse = { throw expectedException }
        val result = petRepository.getAnimalById(111)
        assertEquals(
            expected = Result.failure(expectedException),
            actual = result
        )
    }

    private class FakePetService : PetService {
        var fakeAnimalsResponse: (() -> AnimalsResponse) = {
            AnimalsResponse(animals = emptyList())
        }
        var fakeAnimalResponse: (() -> AnimalResponse) = {
            sampleAnimalResponse()
        }

        override suspend fun getAnimals(limit: Int): AnimalsResponse = fakeAnimalsResponse()

        override suspend fun getAnimalById(id: String): AnimalResponse = fakeAnimalResponse()

    }
}

private fun sampleAnimalsResponse() =
    AnimalsResponse(listOf(sampleAnimalResponse(), sampleAnimalResponse()))

private fun sampleAnimalResponse() = AnimalResponse(
    id = 123,
    url = "https://example.com",
    type = "dog",
    species = "mammal",
    breeds = BreedsResponse(primary = "Labrador", secondary = null, mixed = false, unknown = false),
    age = "Young",
    gender = "Male",
    size = "Medium",
    name = "Buddy",
    description = "A playful Labrador retriever",
    photos = emptyList(),
    status = "status",
    tags = listOf("tag1", "tag2"),
    distance = 4.5
)

private fun sampleAnimal() = Animal(
    id = 123,
    type = "dog",
    species = "mammal",
    breeds = Breed(primary = "Labrador", mixed = false),
    age = "Young",
    gender = "Male",
    size = "Medium",
    name = "Buddy",
    description = "A playful Labrador retriever",
    photo = emptyList(),
    status = "status",
    tags = listOf("tag1", "tag2"),
    distance = 4.5
)