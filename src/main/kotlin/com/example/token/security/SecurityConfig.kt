package com.example.token.security

import com.example.token.security.filters.TokenAuthenticationFilter
import com.example.token.security.filters.TokenVerificationFilter
import com.example.token.security.managers.TokenManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val tokenManager: TokenManager,
    private val authenticationProvider: AuthenticationProvider,
) : WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(authenticationProvider)
        // Required constructor Beans: UserDetailsService, BCryptPasswordEncoder
        // auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder)
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager = super.authenticationManagerBean()

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .formLogin().disable()
            .logout().disable()
            .httpBasic().disable()
            .sessionManagement().sessionCreationPolicy(STATELESS)
            .and()
            .addFilterBefore(
                TokenAuthenticationFilter(tokenManager, authenticationManagerBean()),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .addFilterAfter(
                TokenVerificationFilter(tokenManager),
                TokenAuthenticationFilter::class.java
            )
            .authorizeRequests()
            .antMatchers("/auth/**").permitAll()
            .antMatchers("/users/**").hasAnyAuthority("ROLE_ADMIN")
            .anyRequest().authenticated()
    }

}