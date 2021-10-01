package com.ewan.ciboard.global.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.ewan.ciboard.domain.account.model.domain.Account
import com.ewan.ciboard.global.jwt.JwtProperties.EXPIRATION_TIME
import com.ewan.ciboard.global.jwt.JwtProperties.ISSUER
import com.ewan.ciboard.global.jwt.JwtProperties.SECRET
import com.ewan.ciboard.global.jwt.JwtProperties.USER_ID
import com.ewan.ciboard.global.jwt.JwtProperties.USER_NAME
import com.ewan.ciboard.global.jwt.JwtProperties.USER_ROLE
import java.lang.NullPointerException
import java.util.*

class JwtFactory {
    companion object {
        fun generateJwtToken(account: Account): String {
            return JWT.create()
                .withIssuer(ISSUER)
                .withExpiresAt(Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withClaim(USER_ID, account.id)
                .withClaim(USER_NAME, account.name)
                .withClaim(USER_ROLE, account.role.name)
                .sign(Algorithm.HMAC256(SECRET))
        }

        fun decodeJwt(jwtToken: String): Pair<DecodedJWT?, String> {
            return try{
                val decodedJwt = JWT.require(Algorithm.HMAC256(JwtProperties.SECRET)).build().verify(jwtToken)
                Pair(decodedJwt, "success")
            }catch (e: JWTVerificationException){
                Pair(null, e.message.toString())
            }
        }
    }
}