package com.example.token.repositories

import com.example.token.domain.data.RoleData
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<RoleData, Long> {

    fun findByName(name: String): RoleData?

}