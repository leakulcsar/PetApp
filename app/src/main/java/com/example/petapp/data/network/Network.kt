package com.example.petapp.data.network

import com.example.petapp.domain.repository.TokenRepository
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

fun createAuthService(): AuthService {
    val retrofit = createRetrofit(createClient())
    return retrofit.create()
}

fun createPetService(tokenRepository: TokenRepository): PetService {
    val authInterceptor = AuthInterceptor(tokenRepository)
    val retrofit = createRetrofit(client = createClient(interceptor = authInterceptor))
    return retrofit.create()
}

private fun createRetrofit(client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://api.petfinder.com/v2/")
        .addConverterFactory(MoshiConverterFactory.create())
        .client(client)
        .build()
}

private fun createClient(interceptor: Interceptor? = null): OkHttpClient {
    val builder = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                setLevel(Level.BODY)
            }
        )

    if (interceptor != null) {
        builder.addInterceptor(interceptor)
    }
    return builder.build()
}
