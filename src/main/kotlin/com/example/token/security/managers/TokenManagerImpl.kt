package com.example.token.security.managers

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.example.token.security.utils.DecodedToken
import com.example.token.security.utils.TokenException
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.util.*

@Component
class TokenManagerImpl : TokenManager {

    @Value("\${jwt.secret}")
    private lateinit var secretKey: String

    override fun createAccessToken(
        authentication: Authentication,
        requestUrl: String,
        expirationMinutes: Long
    ): String {
        try {
            return JWT.create()
                .withSubject(authentication.name)
                .withExpiresAt(Date(System.currentTimeMillis() + expirationMinutes * 60 * 1000))
                .withIssuer(requestUrl)
                .withClaim("roles", authentication.authorities.map { it.authority }.toList())
                .sign(Algorithm.HMAC256(secretKey.toByteArray()))
        } catch (e: Exception) {
            throw TokenException(e)
        }
    }

    override fun createRefreshToken(
        authentication: Authentication,
        requestUrl: String,
        expirationMinutes: Long
    ): String {
        try {
            return JWT.create()
                .withSubject(authentication.name)
                .withExpiresAt(Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withIssuer(requestUrl)
                .sign(Algorithm.HMAC256(secretKey.toByteArray()))
        } catch (e: Exception) {
            throw TokenException(e)
        }
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

    private fun decodedJWTFromHeader(
        header: String
    ): DecodedJWT {
        try {
            return JWT
                .require(Algorithm.HMAC256(secretKey.toByteArray()))
                .build()
                .verify(header.substring("Bearer ".length))
        } catch (e: JWTVerificationException) {
            throw TokenException(e)
        }
    }

}