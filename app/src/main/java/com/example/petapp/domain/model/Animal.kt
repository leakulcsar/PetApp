package com.example.petapp.domain.model

data class Animal(
    val id: Int,
    val name: String,
    val breeds: Breed,
    val size: String,
    val gender: String,
    val status: String,
    val distance: Double?,
    val type: String,
    val species: String,
    val age: String,
    val description: String?,
    val photo: List<AnimalPhoto>,
    val tags: List<String>,
)
