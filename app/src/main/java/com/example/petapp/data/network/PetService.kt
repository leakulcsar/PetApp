package com.example.petapp.data.network

import com.example.petapp.data.model.AnimalResponse
import com.example.petapp.data.model.AnimalsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PetService {

    @GET("animals")
    suspend fun getAnimals(@Query("limit") limit: Int = 100): AnimalsResponse

    @GET("animals")
    suspend fun getAnimalById(@Query("id") id: String): AnimalResponse

}