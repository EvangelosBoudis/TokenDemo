package com.example.token.exceptions

import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class DefaultExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [TokenException::class, AuthenticationException::class])
    @ResponseStatus(UNAUTHORIZED)
    fun handleAuthenticationException(
        ex: Exception
    ): ResponseEntity<ErrorDto> {
        return ResponseEntity(
            ErrorDto("AUTHENTICATION_ERROR", ex.message),
            UNAUTHORIZED
        )
    }

}