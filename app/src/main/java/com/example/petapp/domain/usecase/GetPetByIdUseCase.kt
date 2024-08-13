package com.example.petapp.domain.usecase

import com.example.petapp.domain.repository.PetRepository

class GetPetByIdUseCase(
    private val petRepository: PetRepository
) {
    suspend operator fun invoke(id: Int)  = petRepository.getAnimalById(id)
}