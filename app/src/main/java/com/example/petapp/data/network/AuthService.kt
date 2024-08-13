package com.example.petapp.data.network

import com.example.petapp.data.model.TokenResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

private const val PETFINDER_CLIENT_ID = "8FvB92COL3loJkRHBozGPLOVKZTG4CgXal6Dou6EjsH5lj2SXB"
private const val PETFINDER_CLIENT_SECRET = "zcYSA3CrhG6yW1dc539o8rAVgj7ecwLUaYHTSe3s"

interface AuthService {

    @POST("oauth2/token")
    @FormUrlEncoded
    suspend fun getToken(
        @Field("grant_type") grantType: String = "client_credentials",
        @Field("client_id") clientId: String = PETFINDER_CLIENT_ID,
        @Field("client_secret") clientSecret: String = PETFINDER_CLIENT_SECRET
    ): TokenResponse
}