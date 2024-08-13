package com.example.petapp.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnimalsResponse(
    @Json(name = "animals") val animals: List<AnimalResponse>
)
