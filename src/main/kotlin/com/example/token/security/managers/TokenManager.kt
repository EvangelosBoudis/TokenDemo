package com.example.token.security.managers

import com.example.token.domain.dto.TokenDto
import com.example.token.security.utils.DecodedToken
import org.springframework.security.core.Authentication

interface TokenManager {

    fun createTokenDto(
        authentication: Authentication,
        requestUrl: String,
    ): TokenDto

    fun decodedTokenFromHeader(
        header: String
    ): DecodedToken

}