package com.example.petapp.data

import java.time.Instant

interface InstantProvider {
    fun now(): Instant
}

internal class RealInstantProvider : InstantProvider {
    override fun now(): Instant = Instant.now()
}