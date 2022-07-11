package com.example.token.security.utils

enum class Role(val text: String) {
    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN");

    val fullText: String
        get() = "ROLE_$text"
}