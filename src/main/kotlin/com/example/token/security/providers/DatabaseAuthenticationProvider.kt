package com.example.token.security.providers

import com.example.token.repo.UserRepo
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class DatabaseAuthenticationProvider(
    private val userRepo: UserRepo,
    private val passwordEncoder: PasswordEncoder
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        val user = userRepo.findByUsername(authentication.name)
        val passMatches = passwordEncoder.matches(
            authentication.credentials.toString(),
            user.password
        )
        return if (passMatches) user.asUsernamePasswordAuthenticationToken()
        else throw BadCredentialsException("Database authentication failed")
    }

    override fun supports(authentication: Class<*>) =
        authentication == UsernamePasswordAuthenticationToken::class.java
}