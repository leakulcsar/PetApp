package com.example.petapp.data.network

import com.example.petapp.domain.repository.TokenRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenRepository: TokenRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = runBlocking { tokenRepository.getToken().getOrNull() }
        val newRequest = request.newBuilder()
        token?.let { newRequest.header("Authorization", "Bearer ${token.accessToken}") }
        return chain.proceed(newRequest.build())
    }
}