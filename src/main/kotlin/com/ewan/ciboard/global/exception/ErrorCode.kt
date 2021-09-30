package com.ewan.ciboard.global.exception

enum class ErrorCode(
    val status: Int,
    val code: String,
    val message: String

) {
    UNEXPECTED_ERROR(500, "UNEX_001", "UNEXPECTED_ERROR."),

    AUTHENTICATION_FAILED(401, "AUTH_001", "AUTHENTICATION_FAILED."),
    LOGIN_FAILED(401, "AUTH_002", "LOGIN_FAILED."),
    ACCESS_DENIED(401, "AUTH_003", "ACCESS_DENIED."),
    TOKEN_GENERATION_FAILED(500, "AUTH_004", "TOKEN_GENERATION_FAILED."),
}