package com.ewan.ciboard.global.filter

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.ewan.ciboard.domain.account.repository.AccountRepository
import com.ewan.ciboard.global.jwt.JwtProperties.BEARER_PREFIX
import com.ewan.ciboard.global.jwt.JwtProperties.JWT_HEADER
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthorizationFilter(authenticationManager: AuthenticationManager) : BasicAuthenticationFilter(authenticationManager) {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val jwtHeader = request.getHeader(JWT_HEADER)
        if(jwtHeader.isNullOrBlank() || !jwtHeader.startsWith(BEARER_PREFIX)){
            chain.doFilter(request, response)
            return
        }
        val jwtToken = jwtHeader.replace(BEARER_PREFIX, "")
    }
}