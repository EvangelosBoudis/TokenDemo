package com.example.token.security.utils

import org.springframework.security.core.Authentication

interface TokenManager {

    fun createAccessToken(
        authentication: Authentication,
        requestUrl: String,
        expirationMinutes: Long
    ): String

    fun createRefreshToken(
        authentication: Authentication,
        requestUrl: String,
        expirationMinutes: Long
    ): String

    fun decodedTokenFromHeader(
        header: String
    ): DecodedToken

}