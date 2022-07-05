package com.example.token.security.utils

import org.springframework.security.core.userdetails.User

interface TokenManager {

    fun createAccessToken(
        user: User,
        requestUrl: String,
        expirationMinutes: Long
    ): String

    fun createRefreshToken(
        user: User,
        requestUrl: String,
        expirationMinutes: Long
    ): String

    fun decodedTokenFromHeader(
        header: String
    ): DecodedToken

}