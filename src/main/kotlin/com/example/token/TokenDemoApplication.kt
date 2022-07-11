package com.example.token

import com.example.token.domain.data.ClaimData
import com.example.token.domain.data.RoleData
import com.example.token.domain.data.UserData
import com.example.token.security.config.TokenConfigProperties
import com.example.token.security.utils.Claim
import com.example.token.security.utils.Role
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

            val userRole = RoleData(
                Role.ROLE_USER.fullText, listOf(
                    ClaimData(Claim.CLAIM_USER_READ.text),
                    ClaimData(Claim.CLAIM_USER_UPDATE.text)
                )
            )

            val adminRole = RoleData(
                Role.ROLE_ADMIN.fullText, listOf(
                    ClaimData(Claim.CLAIM_USER_CREATE.text),
                    ClaimData(Claim.CLAIM_USER_DELETE.text),
                )
            )

            val user1 = UserData("john", "john@gmail.com", "123", listOf(userRole, adminRole))
            val user2 = UserData("will", "will@gmail.com", "123", listOf(userRole))
            val user3 = UserData("arnold", "arnold@gmail.com", "123", listOf(adminRole))

            userService.saveRole(userRole)
            userService.saveRole(adminRole)

            userService.saveUser(user1)
            userService.saveUser(user2)
            userService.saveUser(user3)
        }
    }

}

fun main(args: Array<String>) {
    runApplication<TokenDemoApplication>(*args)
}



