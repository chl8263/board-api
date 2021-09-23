package com.ewan.ciboard.global.filter

import com.ewan.ciboard.domain.account.model.auth.PrincipalDetails
import com.ewan.ciboard.global.jwt.JwtFactory.Companion.generateJwtToken
import com.ewan.ciboard.global.model.dto.JwtAuthenticationDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JwtAuthenticationFilter(
    processUrl: String
) : AbstractAuthenticationProcessingFilter(processUrl) {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val om = ObjectMapper()
        val authDto = om.readValue(request?.reader, JwtAuthenticationDto::class.java)
        val authenticationToken = UsernamePasswordAuthenticationToken(
            authDto.accountname,
            authDto.password
        )
        val authentication: Authentication = super.getAuthenticationManager().authenticate(authenticationToken)
        return authentication
    }

    override fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, authResult: Authentication) {
        val princialDetail = authResult.principal as PrincipalDetails
        val jwtToken = generateJwtToken(princialDetail.account)
        response.addHeader("Authorization", "Bearer ${jwtToken}")
    }
}