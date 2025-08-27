package com.back.domain.transactions.dto

import java.time.LocalDateTime

data class AccountTransactionDto(
    val id: Int,
    val accountId: Int,
    val type: String,
    val amount: Long,
    val content: String,
    val date: LocalDateTime,
    val createdAt: LocalDateTime,
    val modifyAt: LocalDateTime
)
