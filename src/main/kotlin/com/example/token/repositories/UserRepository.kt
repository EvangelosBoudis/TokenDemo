package com.example.token.repositories

import com.example.token.domain.data.UserData
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserData, Long> {

    fun findByUsername(username: String): UserData?

}

// TODO: Security annotations inside jpa queries