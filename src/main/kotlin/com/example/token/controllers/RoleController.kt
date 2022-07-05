package com.example.token.controllers

import com.example.token.domain.data.RoleData
import com.example.token.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@RestController
@RequestMapping("/roles")
class RoleController(
    private val userService: UserService
) {

    @PostMapping
    fun saveRole(@RequestBody role: RoleData): ResponseEntity<RoleData> {
        val savedRole = userService.saveRole(role)
        val uri = URI.create(
            ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/roles/${savedRole.id}")
                .toUriString()
        )
        return ResponseEntity.created(uri).body(savedRole)
    }

}