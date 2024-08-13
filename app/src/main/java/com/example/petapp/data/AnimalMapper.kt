package com.example.petapp.data

import com.example.petapp.data.model.AnimalResponse
import com.example.petapp.data.model.BreedsResponse
import com.example.petapp.data.model.PhotoResponse
import com.example.petapp.domain.model.Animal
import com.example.petapp.domain.model.AnimalPhoto
import com.example.petapp.domain.model.Breed


fun mapAnimal(animalResponse: AnimalResponse): Animal {
    return Animal(
        id = animalResponse.id,
        name = animalResponse.name,
        breeds = mapBreeds(animalResponse.breeds),
        size = animalResponse.size,
        gender = animalResponse.gender,
        status = animalResponse.status,
        distance = animalResponse.distance,
        type = animalResponse.type,
        species = animalResponse.species,
        age = animalResponse.age,
        description = animalResponse.description,
        photo = mapPhotos(animalResponse.photos),
        tags = mapTags(animalResponse.tags)
    )
}

fun mapTags(tagsResponse: List<String>): List<String> {
    return if (tagsResponse.isEmpty()) {
        emptyList()
    } else {
        tagsResponse.take(3)
    }
}

fun mapBreeds(breedResponse: BreedsResponse): Breed {
    return Breed(primary = breedResponse.primary, mixed = breedResponse.mixed)
}

fun mapPhotos(photos: List<PhotoResponse>): List<AnimalPhoto> {
    return photos.map { photoItem ->
        AnimalPhoto(
            small = photoItem.small,
            large = photoItem.large,
        )
    }
}