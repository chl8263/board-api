package com.ewan.ciboard.domain.account.service

import com.ewan.ciboard.domain.account.model.auth.PrincipalDetails
import com.ewan.ciboard.domain.account.model.domain.Account
import com.ewan.ciboard.domain.account.repository.AccountRepository
import lombok.RequiredArgsConstructor
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class AccountService(
    private val accountRepository: AccountRepository
) : UserDetailsService{

    override fun loadUserByUsername(username: String): UserDetails? {
        val account = accountRepository.findByName(username) ?: return null
        return PrincipalDetails(account)
    }
}