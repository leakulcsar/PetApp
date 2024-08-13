package com.example.petapp.repository

import com.example.petapp.data.InstantProvider
import com.example.petapp.data.model.TokenResponse
import com.example.petapp.data.network.AuthService
import com.example.petapp.data.repository.TokenRepositoryImpl
import com.example.petapp.domain.model.Token
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals


internal class TokenRepositoryImplTest {

    private val instantProvider = FakeInstantProvider()
    private val authService = FakeAuthService()

    @Test
    fun `getToken fetches new token if no cached token available`() = runTest {
        // GIVEN no cached token
        val tokenRepository = TokenRepositoryImpl(
            remoteDataSource = authService,
            instantProvider = instantProvider,
            initialTokenValue = null
        )
        instantProvider.fakeInstant = Instant.parse("2024-08-13T12:00:00Z")
        authService.fakeResponse = {
            sampleTokenResponse().copy(accessToken = "new_access_token", expiresIn = 7200)
        }
        // WHEN getToken is called
        val result = tokenRepository.getToken()
        // THEN new token is fetched from remote data source
        assertEquals(
            expected = Result.success(
                Token(
                    accessToken = "new_access_token",
                    expiresIn = 7200,
                    savedAt = Instant.parse("2024-08-13T12:00:00Z")
                )
            ),
            actual = result
        )
    }

    @Test
    fun `getToken returns cached token if not expired`() = runTest {
        // GIVEN cached token is valid
        val cachedToken = Token(
            accessToken = "cached_access_token",
            expiresIn = 3600,
            savedAt = Instant.parse("2024-08-13T12:00:00Z")
        )
        instantProvider.fakeInstant = Instant.parse("2024-08-13T12:45:00Z")
        val tokenRepository = TokenRepositoryImpl(
            remoteDataSource = authService,
            instantProvider = instantProvider,
            initialTokenValue = cachedToken
        )
        // WHEN getToken is called
        val result = tokenRepository.getToken()
        // THEN cached token is returned
        assertEquals(
            expected = result.getOrNull(),
            actual = cachedToken
        )
    }

    @Test
    fun `getToken fetches new token if cached token expired`() = runTest {
        // GIVEN expired token
        val cachedToken = Token(
            accessToken = "cached_access_token",
            expiresIn = 3600,
            savedAt = Instant.parse("2024-08-13T12:00:00Z")
        )
        val tokenRepository = TokenRepositoryImpl(
            remoteDataSource = authService,
            instantProvider = instantProvider,
            initialTokenValue = cachedToken
        )
        instantProvider.fakeInstant = Instant.parse("2024-08-13T13:01:00Z")
        authService.fakeResponse = {
            sampleTokenResponse().copy(accessToken = "new_access_token", expiresIn = 7200)
        }
        // WHEN getToken is called
        val result = tokenRepository.getToken()
        // THEN new token is fetched from remote data source
        assertEquals(
            expected = Result.success(
                Token(
                    accessToken = "new_access_token",
                    expiresIn = 7200,
                    savedAt = Instant.parse("2024-08-13T13:01:00Z")
                )
            ),
            actual = result
        )
    }

    @Test
    fun `getToken returns error if remote data source fails`() = runTest {
        // GIVEN no cached token
        val tokenRepository = TokenRepositoryImpl(
            remoteDataSource = authService,
            instantProvider = instantProvider,
            initialTokenValue = null
        )
        instantProvider.fakeInstant = Instant.parse("2024-08-13T12:00:00Z")
        val expectedException =
            HttpException(Response.error<TokenResponse>(400, "".toResponseBody()))
        authService.fakeResponse = { throw expectedException }
        // WHEN getToken is called
        val result = tokenRepository.getToken()
        // THEN new token is fetched from remote data source
        assertEquals(
            expected = Result.failure(expectedException),
            actual = result
        )
    }

    private fun sampleTokenResponse() = TokenResponse(
        tokenType = "Bearer",
        expiresIn = 3600,
        accessToken = "access_token"
    )

    private class FakeInstantProvider : InstantProvider {
        var fakeInstant: Instant = Instant.MIN
        override fun now(): Instant = fakeInstant
    }

    private class FakeAuthService : AuthService {

        var fakeResponse: (() -> TokenResponse) = {
            TokenResponse(
                tokenType = "Bearer",
                expiresIn = 3600,
                accessToken = "access_token"
            )
        }

        override suspend fun getToken(
            grantType: String,
            clientId: String,
            clientSecret: String
        ): TokenResponse = fakeResponse()
    }
}