package com.example.token.security.utils

enum class Claim(val text: String) {
    CLAIM_USER_CREATE("user:create"),
    CLAIM_USER_READ("user:read"),
    CLAIM_USER_UPDATE("user:update"),
    CLAIM_USER_DELETE("user:delete");
}