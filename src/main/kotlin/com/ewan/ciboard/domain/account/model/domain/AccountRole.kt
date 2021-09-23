package com.ewan.ciboard.domain.account.model.domain

enum class AccountRole {
    ROLE_ADMIN, ROLE_USER;

    companion object {
        fun getRoleByName(role: String) : AccountRole =
            values().find { x -> x.name == role } ?: throw NoSuchElementException()
    }
}