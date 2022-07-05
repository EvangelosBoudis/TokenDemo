package com.example.token.repo

import com.example.token.domain.data.RoleData
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepo : JpaRepository<RoleData, Long> {

    fun findByName(name: String): RoleData

}