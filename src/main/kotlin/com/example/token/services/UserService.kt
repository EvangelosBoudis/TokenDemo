package com.example.token.services

import com.example.token.domain.data.RoleData
import com.example.token.domain.data.UserData

interface UserService {

    fun saveUser(user: UserData): UserData

    fun saveRole(role: RoleData): RoleData

    fun addRoleToUser(username: String, roleName: String)

    fun getUser(username: String): UserData

    fun getUsers(): List<UserData>

}