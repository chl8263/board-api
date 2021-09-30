package com.ewan.ciboard.global.exception

import com.ewan.ciboard.global.model.response.CommonResponse
import com.ewan.ciboard.global.model.response.ErrorResponse
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(RuntimeException::class)
    protected fun handleRuntimeException(e: RuntimeException): ResponseEntity<ErrorResponse> {
        logger.error { "handleRuntimeException ${e}" }
        val response = ErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            code = "EXCEPTION_01",
            message = e.message ?: "error message not found"
        )
        return ResponseEntity.internalServerError().body(response)
    }

    @ExceptionHandler(InternalAuthenticationServiceException::class)
    protected fun handleInternalAuthenticationServiceException(e: InternalAuthenticationServiceException): ResponseEntity<ErrorResponse> {
        logger.error { "InternalAuthenticationServiceException ${e}" }
        val response = ErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            code = "EXCEPTION_01",
            message = e.message ?: "error message not found"
        )
        return ResponseEntity.internalServerError().body(response)
    }
}