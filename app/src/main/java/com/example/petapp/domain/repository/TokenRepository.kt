package com.example.petapp.domain.repository

import com.example.petapp.domain.model.Token

/**
 * Repository for getting tokens.
 */
interface TokenRepository {

    suspend fun getToken(): Result<Token>

}