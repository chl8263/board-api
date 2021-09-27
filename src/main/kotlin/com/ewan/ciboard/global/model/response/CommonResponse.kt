package com.ewan.ciboard.global.model.response

data class CommonResponse(
    val status: Int,
    val code: String,
    val message: String
)