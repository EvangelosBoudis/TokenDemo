package com.example.token.security.utils

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

data class DecodedToken(
    val subject: String,
    val authorities: List<GrantedAuthority>
) {
    fun asAuthenticationToken(): Authentication = AuthenticationToken(username = subject, authorities = authorities)
}