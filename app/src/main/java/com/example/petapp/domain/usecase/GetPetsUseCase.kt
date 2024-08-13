package com.example.petapp.domain.usecase

import com.example.petapp.domain.repository.PetRepository


class GetPetsUseCase(
    private val petRepository: PetRepository
) {
    suspend operator fun invoke()  = petRepository.getAnimals()
}