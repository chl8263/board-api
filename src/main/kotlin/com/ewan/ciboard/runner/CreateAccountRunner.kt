package com.ewan.ciboard.runner

import com.ewan.ciboard.domain.account.model.domain.Account
import com.ewan.ciboard.domain.account.model.domain.AccountRole
import com.ewan.ciboard.domain.account.repository.AccountRepository
import lombok.RequiredArgsConstructor
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
@RequiredArgsConstructor
class CreateAccountRunner(
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder,
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        val name = "ewan"
        val rawPasswd = "9303"
        val encodedPasswd = passwordEncoder.encode(rawPasswd)
        val account = Account(name = name, password = encodedPasswd)
        accountRepository.save(account)

        val name2 = "ewan2"
        val rawPasswd2 = "9303"
        val encodedPasswd2 = passwordEncoder.encode(rawPasswd2)
        val account2 = Account(name = name2, password = encodedPasswd2, role = AccountRole.ROLE_ADMIN)
        accountRepository.save(account2)
    }
}