package com.example.token.security.filters

import com.example.token.security.managers.TokenManager
import com.example.token.exceptions.TokenException
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TokenVerificationFilter(
    private val tokenManager: TokenManager
) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader(AUTHORIZATION)
        if (authorizationHeader.isNullOrBlank()) throw TokenException("Authorization Header not provided")

        SecurityContextHolder.getContext().authentication = tokenManager
            .decodedTokenFromHeader(authorizationHeader)
            .asAuthenticationToken()

        filterChain.doFilter(request, response)
    }

    @Throws(ServletException::class)
    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return AntPathMatcher().match("/auth/**", request.servletPath)
    }

}