package com.back.backend.domain.transactions.dto

import java.time.LocalDateTime

data class TransactionDto(
    val id: Int,
    val assetId: Int,
    val type: String,
    val amount: Long,
    val content: String,
    val date: LocalDateTime,
    val createdAt: LocalDateTime,
    val modifyAt: LocalDateTime
)
