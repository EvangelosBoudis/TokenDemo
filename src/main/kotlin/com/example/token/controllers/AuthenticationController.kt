package com.example.token.controllers

import com.example.token.domain.criteria.SignInCriteria
import com.example.token.domain.dto.TokenDto
import com.example.token.security.managers.TokenManager
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/auth")
class AuthenticationController(
    private val tokenManager: TokenManager,
    private val authenticationManager: AuthenticationManager
) {

    @PostMapping("/sign-in")
    fun signIn(
        request: HttpServletRequest,
        @RequestBody criteria: SignInCriteria
    ): ResponseEntity<TokenDto> {

        val authentication =
            authenticationManager.authenticate(criteria.asAuthenticationToken())
        val requestUrl = request.requestURL.toString()

        return ResponseEntity(
            tokenManager.createTokenDto(authentication, requestUrl),
            HttpStatus.OK
        )
    }

    @PostMapping("/sign-up")
    fun signUp() {

    }

    @PostMapping("/google-sign-in")
    fun googleSignIn() {

    }

    @PostMapping("/facebook-sign-in")
    fun facebookSignIn() {

    }

}