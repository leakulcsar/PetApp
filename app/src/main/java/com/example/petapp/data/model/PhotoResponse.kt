package com.example.petapp.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class PhotoResponse(
    val small: String,
    val medium: String,
    val large: String,
    val full: String
)
