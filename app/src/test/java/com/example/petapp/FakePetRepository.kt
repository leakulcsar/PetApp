package com.example.petapp

import com.example.petapp.domain.model.Animal
import com.example.petapp.domain.model.Breed
import com.example.petapp.domain.repository.PetRepository

internal class FakePetRepository : PetRepository {

    var animalsResult: Result<List<Animal>> = Result.success(listOf(sampleAnimal()))

    var animalByIdResult: Result<Animal> = Result.success(sampleAnimal())

    override suspend fun getAnimals(): Result<List<Animal>> = animalsResult

    override suspend fun getAnimalById(id: Int): Result<Animal> = animalByIdResult

}

internal fun sampleAnimal() = Animal(
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