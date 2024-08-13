package com.example.petapp.domain.repository

import com.example.petapp.domain.model.Animal

/**
 * Repository for retrieving animals from a remote data source or cache.
 */
interface PetRepository {

    /**
     * Retrieves a list of [Animal] or [Result.failure] if an error occurs.
     */
    suspend fun getAnimals(): Result<List<Animal>>

    /**
     * Retrieves an [Animal] or [Result.failure] if an error occurs.
     */
    suspend fun getAnimalById(id: Int): Result<Animal>
}