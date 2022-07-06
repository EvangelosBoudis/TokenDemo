package com.example.token.security.filters

import com.example.token.security.utils.TokenException
import com.example.token.security.managers.TokenManager
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TokenVerificationFilter(
    private val tokenManager: TokenManager
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader(AUTHORIZATION)
        try {
            val decodedToken = tokenManager.decodedTokenFromHeader(authorizationHeader)
            SecurityContextHolder.getContext().authentication = decodedToken.asAuthenticationToken()
        } catch (e: TokenException) {
            response.status = FORBIDDEN.value()
            response.contentType = APPLICATION_JSON_VALUE
            ObjectMapper().writeValue(
                response.outputStream,
                mapOf("error_message" to e.message)
            )
            return
        }
        filterChain.doFilter(request, response)
    }

}