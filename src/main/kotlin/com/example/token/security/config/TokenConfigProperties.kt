package com.example.token.security.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "token")
data class TokenConfigProperties(
    val secretKey: String,
    val accessKeyMinutes: Long,
    val refreshKeyMinutes: Long
)