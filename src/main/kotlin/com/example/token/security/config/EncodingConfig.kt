package com.example.token.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class EncodingConfig {

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

}