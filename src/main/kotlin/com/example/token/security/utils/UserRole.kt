package com.example.token.security.utils

import com.example.token.security.utils.UserPermission.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

enum class UserRole(private val permissions: Set<UserPermission>) {
    USER(setOf(USER_READ, USER_UPDATE)),
    ADMIN(setOf(USER_CREATE, USER_READ, USER_UPDATE, USER_DELETE));

    val asGrantedAuthorities: Set<GrantedAuthority>
        get() {
            return permissions
                .map { SimpleGrantedAuthority(it.permission) }
                .toMutableList()
                .apply { add(SimpleGrantedAuthority("ROLE_$name")) }
                .toSet()
        }
}