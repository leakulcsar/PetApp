package com.example.petapp.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class BreedsResponse(
    val primary: String,
    val secondary: String?,
    val mixed: Boolean,
    val unknown: Boolean
)
