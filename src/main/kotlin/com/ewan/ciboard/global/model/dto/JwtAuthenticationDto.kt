package com.ewan.ciboard.global.model.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class JwtAuthenticationDto(
    @field:JsonProperty("accountname")
    val accountname: String,

    @field:JsonProperty("password")
    var password: String,
)