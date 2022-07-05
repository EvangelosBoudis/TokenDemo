package com.example.token.security.filters

import com.example.token.security.utils.TokenManager
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TokenAuthenticationFilter(
    private val tokenManager: TokenManager,
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
        val requestUrl = request.requestURL.toString()
        val accessToken = tokenManager.createAccessToken(user, requestUrl, 10, "secret")
        val refreshToken = tokenManager.createRefreshToken(user, requestUrl, 30, "secret")

        response.contentType = APPLICATION_JSON_VALUE
        ObjectMapper().writeValue(
            response.outputStream, mapOf(
                "access_token" to accessToken,
                "refresh_token" to refreshToken,
            )
        )
    }

    // unsuccessfulAuthentication (count unsuccessful logins)

}