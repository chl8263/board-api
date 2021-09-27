package com.ewan.ciboard.runner

import com.ewan.ciboard.domain.account.model.domain.Account
import com.ewan.ciboard.domain.account.model.domain.AccountRole
import com.ewan.ciboard.domain.account.repository.AccountRepository
import lombok.RequiredArgsConstructor
import mu.KotlinLogging
import org.slf4j.LoggerFactory
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

    private val logger = KotlinLogging.logger {}

    override fun run(args: ApplicationArguments?) {

        logger.trace{ "Trace Level 테스트" }
        logger.debug{ "DEBUG Level 테스트"}
        logger.info{ "INFO Level 테스트" }
        logger.warn{ "Warn Level 테스트" }
        logger.error{ "ERROR Level 테스트" }

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