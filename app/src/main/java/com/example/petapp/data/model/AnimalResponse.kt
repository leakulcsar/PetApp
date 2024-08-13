package com.example.petapp.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class AnimalResponse(
    val id: Int,
    val url: String,
    val type: String,
    val species: String,
    val breeds: BreedsResponse,
    val age: String,
    val gender: String,
    val size: String,
    val name: String,
    val description: String?,
    val photos: List<PhotoResponse>,
    val status: String,
    val tags: List<String>,
    val distance: Double?,
)
