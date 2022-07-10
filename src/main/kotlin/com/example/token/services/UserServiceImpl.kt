package com.example.token.services

import com.example.token.domain.data.RoleData
import com.example.token.domain.data.UserData
import com.example.token.repositories.RoleRepository
import com.example.token.repositories.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserServiceImpl(
    private val userRepo: UserRepository,
    private val roleRepo: RoleRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    override fun saveUser(user: UserData): UserData {
        return userRepo.save(user.copy(password = passwordEncoder.encode(user.password)))
    }

    override fun saveRole(role: RoleData) = roleRepo.save(role)

    override fun addRoleToUser(username: String, roleName: String) {
        val user = userRepo.findByUsername(username)
        val role = roleRepo.findByName(roleName)
        user!!.roles.add(role)
    }

    override fun getUser(username: String) = userRepo.findByUsername(username)!!

    override fun getUsers(): List<UserData> = userRepo.findAll()

}