package com.ewan.ciboard.global.filter

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.ewan.ciboard.domain.account.model.auth.PrincipalDetails
import com.ewan.ciboard.domain.account.repository.AccountRepository
import com.ewan.ciboard.global.jwt.HeaderTokenExtractor
import com.ewan.ciboard.global.jwt.JwtProperties
import com.ewan.ciboard.global.jwt.JwtProperties.BEARER_PREFIX
import com.ewan.ciboard.global.jwt.JwtProperties.JWT_HEADER
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JwtAuthorizationFilter(authenticationManager: AuthenticationManager, val accountRepository: AccountRepository, val tokenExtractor: HeaderTokenExtractor) : BasicAuthenticationFilter(authenticationManager) {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val jwtHeader = request.getHeader(JWT_HEADER)
        if(jwtHeader.isNullOrBlank() || !jwtHeader.startsWith(BEARER_PREFIX)){
            chain.doFilter(request, response)
            return
        }

        val jwtToken = tokenExtractor.extract(jwtHeader)
        println("recevied token : ${jwtToken}")
        val username: String = JWT.require(Algorithm.HMAC256(JwtProperties.SECRET)).build().verify(jwtToken)
            .getClaim(JwtProperties.USER_NAME).asString()
        if (username != null) {
            val account = accountRepository.findByName(username) ?: return

            // 인증은 토큰 검증시 끝. 인증을 하기 위해서가 아닌 스프링 시큐리티가 수행해주는 권한 처리를 위해
            // 아래와 같이 토큰을 만들어서 Authentication 객체를 강제로 만들고 그걸 세션에 저장!
            val principalDetails = PrincipalDetails(account)
            val authentication = UsernamePasswordAuthenticationToken(
                principalDetails,  //나중에 컨트롤러에서 DI해서 쓸 때 사용하기 편함.
                null,  // 패스워드는 모르니까 null 처리, 어차피 지금 인증하는게 아니니까!!
                principalDetails.authorities
            )

            // 강제로 시큐리티의 세션에 접근하여 값 저장
            SecurityContextHolder.getContext().authentication = authentication
        }
        println("Authorization comming!")
        chain.doFilter(request, response)
    }
}