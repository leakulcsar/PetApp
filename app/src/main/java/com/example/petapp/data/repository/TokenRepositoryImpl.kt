package com.example.petapp.data.repository

import co.touchlab.stately.concurrency.AtomicReference
import com.example.petapp.data.InstantProvider
import com.example.petapp.data.network.AuthService
import com.example.petapp.domain.model.Token
import com.example.petapp.domain.repository.TokenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class TokenRepositoryImpl(
    private val remoteDataSource: AuthService,
    private val instantProvider: InstantProvider,
    // For testing purposes only, in a real-life project this would be replaced by a "local data source".
    //  That way we could fake/mock the token storage.
    initialTokenValue: Token? = null
) : TokenRepository {

    private var cachedToken: AtomicReference<Token?> = AtomicReference(initialTokenValue)

    override suspend fun getToken(): Result<Token> {
        return cachedToken.get()?.let { cachedToken ->
            if (isTokenExpired(cachedToken)) {
                fetchToken()
            } else {
                Result.success(cachedToken)
            }
        } ?: fetchToken()
    }


    private suspend fun fetchToken(): Result<Token> = withContext(Dispatchers.IO) {
        runCatching { remoteDataSource.getToken() }
            .map {
                Token(
                    accessToken = it.accessToken,
                    expiresIn = it.expiresIn,
                    savedAt = instantProvider.now()
                )
            }
            .onSuccess { cachedToken.set(it) }
    }

    private fun isTokenExpired(token: Token): Boolean {
        val expirationTime = token.savedAt.plusSeconds(token.expiresIn)
        return expirationTime.isBefore(instantProvider.now())
    }

}