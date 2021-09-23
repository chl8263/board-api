package com.ewan.ciboard.domain.account.model.auth

import com.ewan.ciboard.domain.account.model.domain.Account
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/*
*   Security session -> Authentication -> UserDetail
* */

class PrincipalDetails(val account: Account) : UserDetails {
    override fun getPassword(): String {
        return account.password
    }

    override fun getUsername(): String {
        return account.name
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        val authorities: MutableCollection<GrantedAuthority> = ArrayList()
        authorities.add(GrantedAuthority { account.role.name })
        return authorities
    }
}