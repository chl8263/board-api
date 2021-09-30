package com.ewan.ciboard.global.model.response

data class CustomFieldError(
    val field: String,
    val value: Any,
    val reason: String
)