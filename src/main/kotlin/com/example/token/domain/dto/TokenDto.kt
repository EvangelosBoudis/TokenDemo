package com.example.token.domain.dto

data class TokenDto(
    val accessToken: String,
    val refreshToken: String
)