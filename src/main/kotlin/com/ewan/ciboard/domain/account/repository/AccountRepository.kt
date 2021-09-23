package com.ewan.ciboard.domain.account.repository

import com.ewan.ciboard.domain.account.model.domain.Account
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<Account, Long> {
    fun findByName(name: String): Account?
}