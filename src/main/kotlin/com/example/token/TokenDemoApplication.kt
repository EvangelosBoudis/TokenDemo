package com.example.token

import com.example.token.domain.data.RoleData
import com.example.token.domain.data.UserData
import com.example.token.security.config.TokenConfigProperties
import com.example.token.services.UserService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

// TODO: Facebook, Google-Sing in copy CouchbaseSyncGateway, integration testing/ auto documentation

@SpringBootApplication
@EnableConfigurationProperties(TokenConfigProperties::class)
class TokenDemoApplication {

    @Bean
    fun run(userService: UserService): CommandLineRunner {
        return CommandLineRunner {
            userService.saveRole(RoleData(name = "ROLE_USER"))
            userService.saveRole(RoleData(name = "ROLE_MANAGER"))
            userService.saveRole(RoleData(name = "ROLE_ADMIN"))
            userService.saveRole(RoleData(name = "ROLE_SUPER_ADMIN"))

            userService.saveUser(UserData(name = "John Travolta", username = "john", password = "1234"))
            userService.saveUser(UserData(name = "Will Smith", username = "will", password = "1234"))
            userService.saveUser(UserData(name = "Jim Carry", username = "jim", password = "1234"))
            userService.saveUser(UserData(name = "Arnold Schwarzenegger", username = "arnold", password = "1234"))

            userService.addRoleToUser("john", "ROLE_USER")
            userService.addRoleToUser("john", "ROLE_MANAGER")
            userService.addRoleToUser("will", "ROLE_MANAGER")
            userService.addRoleToUser("jim", "ROLE_ADMIN")
            userService.addRoleToUser("arnold", "ROLE_SUPER_ADMIN")
            userService.addRoleToUser("arnold", "ROLE_ADMIN")
            userService.addRoleToUser("arnold", "ROLE_USER")
        }
    }

}

fun main(args: Array<String>) {
    runApplication<TokenDemoApplication>(*args)
}



