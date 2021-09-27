package com.ewan.ciboard.global.config

import com.ewan.ciboard.domain.account.repository.AccountRepository
import com.ewan.ciboard.global.filter.JwtAuthenticationFilter
import com.ewan.ciboard.global.filter.JwtAuthorizationFilter
import com.ewan.ciboard.global.jwt.HeaderTokenExtractor
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.filter.CorsFilter

@Configuration
@EnableWebSecurity  // The SpringSecurity filter will join with Spring filter chain
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // Active 'Secured', 'preAuthorize' annotation
@RequiredArgsConstructor
class SecurityConfig(
    private val corsFilter: CorsFilter,
    private val accountRepository: AccountRepository,
    private val headerTokenExtractor: HeaderTokenExtractor
) : WebSecurityConfigurerAdapter() {

    private val PREFIX_API_VER = "/api/v1"

    @Bean
    fun encodePwd(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
            .formLogin().disable()
            .httpBasic().disable()

            .addFilter(corsFilter)
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .addFilter(jwtAuthorizationFilter())

            .authorizeRequests()
            .antMatchers("${PREFIX_API_VER}/user/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
            .antMatchers("${PREFIX_API_VER}/admin/**").access("hasRole('ROLE_ADMIN')")
            .anyRequest().permitAll()
    }

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers(HttpMethod.POST, "${PREFIX_API_VER}/account/enroll")
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**")
    }

    private fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        val filter = JwtAuthenticationFilter("/login")
        filter.setAuthenticationManager(authenticationManager())
        return filter
    }

    private fun jwtAuthorizationFilter(): JwtAuthorizationFilter {
        return JwtAuthorizationFilter(authenticationManager(), headerTokenExtractor)
    }
}