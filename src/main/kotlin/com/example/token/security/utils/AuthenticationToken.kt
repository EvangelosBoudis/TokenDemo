package com.example.token.security.utils

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

data class AuthenticationToken(
    private val username: String,
    private val password: String? = null,
    private val authorities: List<GrantedAuthority> = emptyList(),
    private val details: Map<String, Any> = emptyMap()
) : Authentication {
    override fun getName() = username
    override fun getAuthorities() = authorities.toMutableList()
    override fun getCredentials() = password
    override fun getDetails() = details
    override fun getPrincipal() = username
    override fun isAuthenticated() = authorities.isNotEmpty()
    override fun setAuthenticated(isAuthenticated: Boolean) {}
}