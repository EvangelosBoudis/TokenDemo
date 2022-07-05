package com.example.token.security.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.util.*

@Component
class TokenManagerImpl : TokenManager {

    override fun createAccessToken(
        user: User,
        requestUrl: String,
        expirationMinutes: Long,
        secretKey: String
    ): String {
        return JWT.create()
            .withSubject(user.username)
            .withExpiresAt(Date(System.currentTimeMillis() + expirationMinutes * 60 * 1000)) // (milliseconds) 10 minutes
            .withIssuer(requestUrl)
            .withClaim("roles", user.authorities.map { it.authority }.toList())
            .sign(Algorithm.HMAC256(secretKey.toByteArray()))
    }

    override fun createRefreshToken(
        user: User,
        requestUrl: String,
        expirationMinutes: Long,
        secretKey: String
    ): String {
        return JWT.create()
            .withSubject(user.username)
            .withExpiresAt(Date(System.currentTimeMillis() + 30 * 60 * 1000)) // (milliseconds) 30 minutes
            .withIssuer(requestUrl)
            .sign(Algorithm.HMAC256(secretKey.toByteArray()))
    }

}