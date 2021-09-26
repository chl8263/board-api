package com.ewan.ciboard.global.jwt

import org.springframework.stereotype.Component
import java.security.InvalidKeyException

@Component
class HeaderTokenExtractor {

    companion object{
        val HEADER_PREFIX = "Bearer "
    }

    fun extract(header: String): String{
        if(header.isEmpty() || header.length < HEADER_PREFIX.length){
            throw InvalidKeyException("This is not valid token information")
        }

        return header.substring(HEADER_PREFIX.length, header.length)
    }

}