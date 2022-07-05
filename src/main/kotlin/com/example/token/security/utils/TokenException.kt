package com.example.token.security.utils

class TokenException(message: String?) : Exception(message) {
    constructor(throwable: Throwable) : this(throwable.message)
}