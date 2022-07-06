package com.example.token.security.filters

import com.example.token.security.utils.AuthenticationToken
import com.example.token.security.managers.TokenManager
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TokenAuthenticationFilter(
    private val tokenManager: TokenManager,
    authenticationManager: AuthenticationManager,
) : AbstractAuthenticationProcessingFilter(
    AntPathRequestMatcher("/auth/sign-in", "POST"),
    authenticationManager
) {

    override fun attemptAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): Authentication {
        val authenticationToken = AuthenticationToken(
            request.getParameter("username"),
            request.getParameter("password")
        )
        return authenticationManager.authenticate(authenticationToken)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authentication: Authentication
    ) {
        val requestUrl = request.requestURL.toString()
        val accessToken = tokenManager.createAccessToken(authentication, requestUrl, 10)
        val refreshToken = tokenManager.createRefreshToken(authentication, requestUrl, 30)
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