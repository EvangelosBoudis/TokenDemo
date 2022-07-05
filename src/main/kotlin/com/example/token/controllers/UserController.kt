package com.example.token.controllers

import com.example.token.domain.data.UserData
import com.example.token.domain.dto.RoleDto
import com.example.token.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {

    @GetMapping
    fun getUsers(): ResponseEntity<List<UserData>> {
        val users = userService.getUsers()
        return ResponseEntity.ok().body(users)
    }

    @PostMapping
    fun saveUser(@RequestBody user: UserData): ResponseEntity<UserData> {
        val savedUser = userService.saveUser(user)
        val uri = URI.create(
            ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/users/${savedUser.id}")
                .toUriString()
        )
        return ResponseEntity.created(uri).body(savedUser)
    }

    @PostMapping("/{username}/role")
    fun addRoleToUser(
        @PathVariable username: String,
        @RequestBody role: RoleDto
    ): ResponseEntity<Any> {
        userService.addRoleToUser(username, role.name)
        return ResponseEntity.ok().build()
    }

    fun refreshToken(
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {


    }

}