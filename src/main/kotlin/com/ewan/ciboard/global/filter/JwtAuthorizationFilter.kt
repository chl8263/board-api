package com.ewan.ciboard.global.filter

import com.ewan.ciboard.global.jwt.HeaderTokenExtractor
import com.ewan.ciboard.global.jwt.JwtFactory
import com.ewan.ciboard.global.jwt.JwtProperties
import com.ewan.ciboard.global.jwt.JwtProperties.BEARER_PREFIX
import com.ewan.ciboard.global.jwt.JwtProperties.JWT_HEADER
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JwtAuthorizationFilter(authenticationManager: AuthenticationManager,
                             private val tokenExtractor: HeaderTokenExtractor) : BasicAuthenticationFilter(authenticationManager) {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val jwtHeader = request.getHeader(JWT_HEADER)
        if(jwtHeader.isNullOrBlank() || !jwtHeader.startsWith(BEARER_PREFIX)){
            chain.doFilter(request, response)
            return
        }

        val jwtToken = tokenExtractor.extract(jwtHeader)
        println("recevied token : ${jwtToken}")
        val decodedJwtToken = JwtFactory.decodeJwt(jwtToken)
        val decodedUserName = decodedJwtToken.getClaim(JwtProperties.USER_NAME).asString() ?: throw RuntimeException()
        val decodedUserRole = decodedJwtToken.getClaim(JwtProperties.USER_ROLE).asString() ?: throw RuntimeException()

        val authenticationToken = UsernamePasswordAuthenticationToken(
            decodedUserName,
            null,
            listOf(SimpleGrantedAuthority(decodedUserRole))
        )
        SecurityContextHolder.getContext().authentication = authenticationToken
        chain.doFilter(request, response)
    }
}