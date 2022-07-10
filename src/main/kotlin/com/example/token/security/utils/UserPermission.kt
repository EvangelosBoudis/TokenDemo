package com.example.token.security.utils

enum class UserPermission(val permission: String) {
    USER_CREATE("user:create"),
    USER_READ("user:read"),
    USER_UPDATE("user:update"),
    USER_DELETE("user:delete");
}