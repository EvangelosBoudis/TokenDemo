package com.example.token.security.managers

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.example.token.domain.dto.TokenDto
import com.example.token.security.utils.DecodedToken
import com.example.token.exceptions.TokenException
import com.example.token.security.config.TokenConfigProperties
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.util.*

@Component
class TokenManagerImpl(
    private val tokenConfigProperties: TokenConfigProperties
) : TokenManager {

    override fun createTokenDto(
        authentication: Authentication,
        requestUrl: String
    ): TokenDto {
        return TokenDto(
            createAccessToken(authentication, requestUrl),
            createRefreshToken(authentication, requestUrl)
        )
    }

    override fun decodedTokenFromHeader(
        header: String
    ): DecodedToken {
        val token = decodedJWTFromHeader(header)
        val claims = token.claims["roles"] ?: throw TokenException("ROLES_NOT_FOUND")
        val authorities = claims
            .asArray(String::class.java)
            .map { SimpleGrantedAuthority(it) }
        return DecodedToken(token.subject, authorities)
    }

    fun createAccessToken(
        authentication: Authentication,
        requestUrl: String
    ): String {
        try {
            return JWT.create()
                .withSubject(authentication.name)
                .withExpiresAt(Date(System.currentTimeMillis() + tokenConfigProperties.accessKeyMinutes * 60 * 1000))
                .withIssuer(requestUrl)
                .withClaim("roles", authentication.authorities.map { it.authority }.toList())
                .sign(Algorithm.HMAC256(tokenConfigProperties.secretKey.toByteArray()))
        } catch (ex: Exception) {
            throw TokenException(ex)
        }
    }

    fun createRefreshToken(
        authentication: Authentication,
        requestUrl: String
    ): String {
        try {
            return JWT.create()
                .withSubject(authentication.name)
                .withExpiresAt(Date(System.currentTimeMillis() + tokenConfigProperties.refreshKeyMinutes * 60 * 1000))
                .withIssuer(requestUrl)
                .sign(Algorithm.HMAC256(tokenConfigProperties.secretKey.toByteArray()))
        } catch (ex: Exception) {
            throw TokenException(ex)
        }
    }

    private fun decodedJWTFromHeader(
        header: String
    ): DecodedJWT {
        try {
            return JWT
                .require(Algorithm.HMAC256(tokenConfigProperties.secretKey.toByteArray()))
                .build()
                .verify(header.substring("Bearer ".length))
        } catch (ex: JWTVerificationException) {
            throw TokenException(ex)
        }
    }

}