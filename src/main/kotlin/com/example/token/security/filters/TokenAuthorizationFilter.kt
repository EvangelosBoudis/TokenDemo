package com.example.token.security.filters

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TokenAuthorizationFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // request.servletPath.equals("/sign-in")
        val authorizationHeader = request.getHeader(AUTHORIZATION)
        if (!authorizationHeader.isNullOrEmpty() &&
            authorizationHeader.startsWith(BEARER)
        ) {
            try {
                val algorithm = Algorithm.HMAC256("secret".toByteArray())
                val token = authorizationHeader.substring(BEARER.length)
                val decodedToken = JWT
                    .require(algorithm)
                    .build()
                    .verify(token)
                val authorities = decodedToken.claims["roles"]
                    ?.asArray(String::class.java)
                    ?.map { SimpleGrantedAuthority(it) } ?: emptyList()

                SecurityContextHolder.getContext().authentication =
                    UsernamePasswordAuthenticationToken(
                        decodedToken.subject,
                        null,
                        authorities
                    )
            } catch (e: JWTVerificationException) {
                response.status = FORBIDDEN.value()
                response.contentType = APPLICATION_JSON_VALUE
                ObjectMapper().writeValue(
                    response.outputStream,
                    mapOf("error_message" to e.message)
                )
                return
            }
        }
        filterChain.doFilter(request, response)
    }

    companion object {
        private const val BEARER = "Bearer "
    }

}