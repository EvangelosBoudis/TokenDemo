package com.example.token.security.filters

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TokenAuthenticationFilter(
    authenticationManager: AuthenticationManager, // in order to authenticate the user
) : AbstractAuthenticationProcessingFilter(
    AntPathRequestMatcher("/auth/sign-in", "POST"),
    authenticationManager
) {

    // whenever user tries to log in
    override fun attemptAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): Authentication {
        // TODO: double bcrypt decryption
        val authenticationToken = UsernamePasswordAuthenticationToken(
            request.getParameter("username"),
            request.getParameter("password")
        ) // TODO: Replace with Body (Object Mapper)
        return authenticationManager.authenticate(authenticationToken)
    }

    // whenever successfully logs in
    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authentication: Authentication
    ) {
        val user = authentication.principal as User
        val algorithm = Algorithm.HMAC256("secret".toByteArray())
        val issuer = request.requestURL.toString()

        val accessToken = JWT.create()
            .withSubject(user.username)
            .withExpiresAt(Date(System.currentTimeMillis() + 10 * 60 * 1000)) // (milliseconds) 10 minutes
            .withIssuer(issuer)
            .withClaim("roles", user.authorities.map { it.authority }.toList())
            .sign(algorithm)

        val refreshToken = JWT.create()
            .withSubject(user.username)
            .withExpiresAt(Date(System.currentTimeMillis() + 30 * 60 * 1000)) // (milliseconds) 30 minutes
            .withIssuer(issuer)
            .sign(algorithm)

        val tokens = mapOf(
            "access_token" to accessToken,
            "refresh_token" to refreshToken,
        )
        response.contentType = APPLICATION_JSON_VALUE
        ObjectMapper().writeValue(response.outputStream, tokens)

        /*response.setHeader("access_token", accessToken)
        response.setHeader("refresh_token", refreshToken)*/
    }

    // unsuccessfulAuthentication (count unsuccessful logins)

}