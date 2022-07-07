package com.example.token.security.config

import com.example.token.security.filters.TokenAuthenticationFilter
import com.example.token.security.filters.TokenVerificationFilter
import com.example.token.security.managers.TokenManager
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class TokenConfig(
    private val tokenManager: TokenManager
) : AbstractHttpConfigurer<TokenConfig, HttpSecurity>() {

    override fun configure(http: HttpSecurity) {
        http.addFilterBefore(
            TokenAuthenticationFilter(tokenManager, http.getSharedObject(AuthenticationManager::class.java)),
            UsernamePasswordAuthenticationFilter::class.java
        ).addFilterAfter(
            TokenVerificationFilter(tokenManager),
            TokenAuthenticationFilter::class.java
        )
    }

}