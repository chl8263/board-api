package com.ewan.ciboard.domain.account.api

import com.ewan.ciboard.domain.account.model.domain.Account
import com.ewan.ciboard.domain.account.repository.AccountRepository
import com.ewan.ciboard.global.jwt.JwtFactory
import lombok.RequiredArgsConstructor
import org.springframework.security.access.annotation.Secured
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/v1/account"])
@RequiredArgsConstructor
class AccountController (
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder,
        ) {

    @PostMapping("/enroll")
    fun enroll(@RequestBody account: Account): String {
        val rawPasswd = account.password
        val encodedPasswd = passwordEncoder.encode(rawPasswd)
        account.password = encodedPasswd
        accountRepository.save(account)
        return "enroll"
    }
}