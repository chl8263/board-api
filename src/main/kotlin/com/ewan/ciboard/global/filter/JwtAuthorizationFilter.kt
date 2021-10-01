package com.ewan.ciboard.global.filter

import com.ewan.ciboard.global.exception.ErrorCode
import com.ewan.ciboard.global.jwt.HeaderTokenExtractor
import com.ewan.ciboard.global.jwt.JwtFactory
import com.ewan.ciboard.global.jwt.JwtProperties
import com.ewan.ciboard.global.jwt.JwtProperties.BEARER_PREFIX
import com.ewan.ciboard.global.jwt.JwtProperties.JWT_HEADER
import com.ewan.ciboard.global.model.response.ErrorResponse
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JwtAuthorizationFilter(authenticationManager: AuthenticationManager, private val tokenExtractor: HeaderTokenExtractor, val om: ObjectMapper = jacksonObjectMapper()) : BasicAuthenticationFilter(authenticationManager) {

    private val logger = KotlinLogging.logger {}

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val jwtHeader = request.getHeader(JWT_HEADER)
        if(jwtHeader.isNullOrBlank() || !jwtHeader.startsWith(BEARER_PREFIX)){
            chain.doFilter(request, response)
            return
        }

        val jwtToken = tokenExtractor.extract(jwtHeader)
        val decodedJwtTokenInfo = JwtFactory.decodeJwt(jwtToken)
        val decodedJwtToken = decodedJwtTokenInfo.first
        if(decodedJwtToken != null) {
            val decodedUserName = decodedJwtToken.getClaim(JwtProperties.USER_NAME).asString()
            val decodedUserRole = decodedJwtToken.getClaim(JwtProperties.USER_ROLE).asString()
            val authenticationToken = UsernamePasswordAuthenticationToken(
                decodedUserName,
                null,
                listOf(SimpleGrantedAuthority(decodedUserRole))
            )
            SecurityContextHolder.getContext().authentication = authenticationToken
            chain.doFilter(request, response)
        }else {
            unSuccessHandler(request, response, decodedJwtTokenInfo.second)
        }
    }

    private fun unSuccessHandler(request: HttpServletRequest, response: HttpServletResponse, e: String){
        val  responseModel = ErrorResponse(
            status = ErrorCode.AUTHORIZATION_FAILED.status,
            code = ErrorCode.AUTHORIZATION_FAILED.code,
            message = e
        )
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.writer.write(om.writeValueAsString(responseModel))
    }
}