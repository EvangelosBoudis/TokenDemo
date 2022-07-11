package com.example.token.security.providers

import com.example.token.repositories.UserRepository
import com.example.token.security.utils.AuthenticationToken
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class TokenAuthenticationProvider(
    private val userRepo: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : AuthenticationProvider {

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication {
        userRepo.findByUsername(authentication.name)?.let { user ->
            val passMatches = passwordEncoder.matches(
                authentication.credentials.toString(),
                user.passwordHash
            )
            if (passMatches) return user.asAuthenticationToken()
        }
        throw BadCredentialsException("Invalid username or password")
    }

    override fun supports(authentication: Class<*>) =
        authentication == AuthenticationToken::class.java
}