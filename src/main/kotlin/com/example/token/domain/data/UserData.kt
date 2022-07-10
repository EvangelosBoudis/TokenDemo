package com.example.token.domain.data

import com.example.token.security.utils.AuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
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
    fun asAuthenticationToken() = AuthenticationToken(
        username,
        password,
        roles.map { SimpleGrantedAuthority(it.name) }
    )
}