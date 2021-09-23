package com.ewan.ciboard.domain.account.model.domain

import org.hibernate.annotations.CreationTimestamp
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.*
import org.springframework.data.jpa.repository.Temporal


@Entity
class Account (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(unique = true)
    var name: String,

    @Column
    var password: String,

    @Column
    @Enumerated(EnumType.STRING)
    var role: AccountRole = AccountRole.ROLE_USER,

    //@CreationTimestamp
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    var createDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date()),

    //@CreationTimestamp
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    var loginDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date()),

    //@CreationTimestamp
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    var modifyDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date()),

    var active: Int = 1
)