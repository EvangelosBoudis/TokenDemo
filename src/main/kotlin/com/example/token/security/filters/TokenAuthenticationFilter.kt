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

// This Filter is not used by SecurityConfig, just ignore it

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
        val tokenDto = tokenManager.createTokenDto(
            authentication,
            request.requestURL.toString()
        )
        response.contentType = APPLICATION_JSON_VALUE
        ObjectMapper().writeValue(response.outputStream, tokenDto)
    }

    // unsuccessfulAuthentication (count unsuccessful logins)

}