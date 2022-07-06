package com.example.token.domain.data

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import javax.persistence.*

@Entity
@Table(name = "users")
data class UserData(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    val name: String,
    val username: String,
    val password: String,
    @ManyToMany(fetch = FetchType.EAGER)
    val roles: MutableCollection<RoleData> = mutableSetOf()
) {
    fun asDetailedUser() = User(
        username,
        password,
        roles.map { SimpleGrantedAuthority(it.name) }
    )

    fun asUsernamePasswordAuthenticationToken() = UsernamePasswordAuthenticationToken(
        username,
        password,
        roles.map { SimpleGrantedAuthority(it.name) }
    )
}