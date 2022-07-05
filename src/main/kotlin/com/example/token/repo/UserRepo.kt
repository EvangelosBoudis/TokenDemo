package com.example.token.repo

import com.example.token.domain.data.UserData
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepo : JpaRepository<UserData, Long> {

    fun findByUsername(username: String): UserData

}