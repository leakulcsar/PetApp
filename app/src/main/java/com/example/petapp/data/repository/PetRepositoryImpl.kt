package com.example.petapp.data.repository

import com.example.petapp.data.mapAnimal
import com.example.petapp.data.network.PetService
import com.example.petapp.domain.model.Animal
import com.example.petapp.domain.repository.PetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class PetRepositoryImpl(
    private val remoteDataSource: PetService,
    // For testing purposes only, in a real-life project this would be replaced by a "local data source".
    initialCachedAnimals: List<Animal> = emptyList()
) : PetRepository {

    private var cachedAnimalList: List<Animal> = initialCachedAnimals

    override suspend fun getAnimals(): Result<List<Animal>> = withContext(Dispatchers.IO) {
        runCatching {
            val animals = remoteDataSource.getAnimals().animals.map {
                mapAnimal(it)
            }
            cachedAnimalList = animals
            return@runCatching cachedAnimalList
        }
    }

    override suspend fun getAnimalById(id: Int): Result<Animal> {
        val cachedAnimal = cachedAnimalList.find { it.id == id }
        return if (cachedAnimal == null) {
            fetchAnimalById(id)
        } else {
            Result.success(cachedAnimal)
        }
    }

    suspend fun fetchAnimalById(id: Int): Result<Animal> = withContext(Dispatchers.IO) {
        runCatching {
            val animalResponse = remoteDataSource.getAnimalById(id.toString())
            return@runCatching mapAnimal(animalResponse)
        }
    }
}