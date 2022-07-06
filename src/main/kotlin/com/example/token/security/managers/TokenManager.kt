package com.example.token.security.managers

import com.example.token.security.utils.DecodedToken
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