package com.ewan.ciboard.global.filter

import com.ewan.ciboard.domain.account.model.auth.PrincipalDetails
import com.ewan.ciboard.global.exception.ErrorCode
import com.ewan.ciboard.global.jwt.JwtFactory
import com.ewan.ciboard.global.model.dto.JwtAuthenticationDto
import com.ewan.ciboard.global.model.response.ErrorResponse
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter(private val authenticationManager: AuthenticationManager, val om: ObjectMapper = jacksonObjectMapper()) : OncePerRequestFilter() {
    private val logger = KotlinLogging.logger {}

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            val authDto = om.readValue(request.reader, JwtAuthenticationDto::class.java)
            val authenticationToken = UsernamePasswordAuthenticationToken(
                authDto.accountname,
                authDto.password
            )
            val authentication = authenticationManager.authenticate(authenticationToken)
            successHandler(request, response, authentication)
        }catch (e: AuthenticationException){
            unSuccessHandler(request, response, e)
        }catch (e: Exception){
            logger.error { "${e.printStackTrace()}" }
            unSuccessHandler(request, response, e)
        }
    }

    private fun successHandler(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication){
        val principalDetail = authentication.principal as PrincipalDetails
        val jwtToken = JwtFactory.generateJwtToken(principalDetail.account)
        response.status = HttpStatus.OK.value()
        response.writer.write(om.writeValueAsString((hashMapOf("jwtToken" to jwtToken))))
    }

    private fun unSuccessHandler(request: HttpServletRequest, response: HttpServletResponse, e: Exception){
        val responseModel: ErrorResponse
        when(e) {
            is AuthenticationException -> {
                responseModel = ErrorResponse(
                    status = ErrorCode.AUTHENTICATION_FAILED.status,
                    code = ErrorCode.AUTHENTICATION_FAILED.code,
                    message = ErrorCode.AUTHENTICATION_FAILED.message
                )
            }
            else -> {
                responseModel = ErrorResponse(
                    status = ErrorCode.UNEXPECTED_ERROR.status,
                    code = ErrorCode.UNEXPECTED_ERROR.code,
                    message = ErrorCode.UNEXPECTED_ERROR.message
                )
            }
        }
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.writer.write(om.writeValueAsString(responseModel))
    }
}