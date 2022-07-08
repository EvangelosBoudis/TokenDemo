package com.example.token.domain.criteria

import com.example.token.security.utils.AuthenticationToken

data class SignInCriteria(
    val username: String,
    val password: String
) {
    fun asAuthenticationToken() = AuthenticationToken(username, password)
}