package com.example.petapp.domain.model

import java.time.Instant

data class Token(
    val accessToken: String,
    val expiresIn: Long,
    val savedAt: Instant
)