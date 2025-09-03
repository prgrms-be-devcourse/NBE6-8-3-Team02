package com.back.domain.transactions.dto

import com.back.domain.transactions.entity.AccountTransaction
import java.time.LocalDateTime

data class AccountTransactionDto(
    val id: Int,
    val accountId: Int,
    val type: String,
    val amount: Long,
    val content: String,
    val date: LocalDateTime,
    val createDate: LocalDateTime,
    val modifyDate: LocalDateTime
) {
    constructor (accountTransaction: AccountTransaction) : this(
        id = accountTransaction.id,
        accountId = accountTransaction.account.id,
        type = accountTransaction.type.name,
        amount = accountTransaction.amount,
        content = accountTransaction.content,
        date = accountTransaction.date,
        createDate = accountTransaction.createDate,
        modifyDate = accountTransaction.modifyDate,
    )
}
