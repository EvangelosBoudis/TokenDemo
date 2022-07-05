package com.example.token.security

import com.example.token.security.filters.TokenAuthenticationFilter
import com.example.token.security.filters.TokenAuthorizationFilter
import com.example.token.security.utils.TokenManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val tokenManager: TokenManager,
    private val userDetailsService: UserDetailsService,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
) : WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(bCryptPasswordEncoder)
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager = super.authenticationManagerBean()

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .formLogin().disable()
            .logout().disable()
            .sessionManagement().sessionCreationPolicy(STATELESS)
            .and().authorizeRequests().antMatchers("/auth/**").permitAll()
            .and().authorizeRequests().antMatchers("/users/**").hasAnyAuthority("ROLE_ADMIN")
            .and().authorizeRequests().anyRequest().authenticated()
            .and().addFilterBefore(
                TokenAuthenticationFilter(tokenManager, authenticationManagerBean()),
                UsernamePasswordAuthenticationFilter::class.java
            ).addFilterBefore(
                TokenAuthorizationFilter(tokenManager),
                UsernamePasswordAuthenticationFilter::class.java
            )
    }

}