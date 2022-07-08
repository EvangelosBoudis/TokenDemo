package com.example.token.exceptions

class TokenException(message: String?) : Exception(message) {
    constructor(throwable: Throwable) : this(throwable.message)
}