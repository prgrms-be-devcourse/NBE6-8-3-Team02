package com.back.domain.transactions.dto

data class UpdateTransactionRequestDto(
    val id: Int,
    val type: String,
    val amount: Long,
    val content: String,
    val date: String
)
