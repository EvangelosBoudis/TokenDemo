package com.example.token.security.utils

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority

data class DecodedToken(
    val subject: String,
    val authorities: List<GrantedAuthority>
) {
    fun asUsernamePasswordAuthenticationToken(): UsernamePasswordAuthenticationToken {
        return UsernamePasswordAuthenticationToken(subject, null, authorities)
    }
}