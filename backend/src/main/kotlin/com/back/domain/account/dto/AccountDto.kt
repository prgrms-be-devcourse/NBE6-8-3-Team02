package com.back.domain.account.dto

import com.back.domain.account.entity.Account
import java.time.LocalDateTime

data class AccountDtoKt(
    val id: Int,
    val memberId: Int,
    val name: String,
    val accountNumber: String,
    val balance: Long,
    val createDate: LocalDateTime,
    val modifyDate: LocalDateTime
) {
    constructor(account: Account) : this(
        id = account.id,
        memberId = account.member.id,
        name = account.name,
        accountNumber = account.accountNumber,
        balance = account.balance,
        createDate = account.createDate,
        modifyDate = account.modifyDate
    )
}


