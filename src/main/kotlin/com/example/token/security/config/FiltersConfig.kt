package com.example.token.security.config

import com.example.token.security.filters.ExceptionHandlerFilter
import com.example.token.security.filters.TokenVerificationFilter
import com.example.token.security.managers.TokenManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.web.filter.CorsFilter
import org.springframework.web.servlet.HandlerExceptionResolver

class FiltersConfig(
    private val tokenManager: TokenManager,
    private val resolver: HandlerExceptionResolver
) : AbstractHttpConfigurer<FiltersConfig, HttpSecurity>() {

    override fun configure(http: HttpSecurity) {
        http
            .addFilterBefore(
                ExceptionHandlerFilter(resolver),
                CorsFilter::class.java
            ).addFilterAfter(
                TokenVerificationFilter(tokenManager),
                ExceptionHandlerFilter::class.java
            )
    }

}