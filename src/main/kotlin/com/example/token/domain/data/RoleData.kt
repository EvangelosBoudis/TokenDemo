package com.example.token.domain.data

import javax.persistence.*

@Entity
@Table(name = "roles")
data class RoleData(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    val name: String
)