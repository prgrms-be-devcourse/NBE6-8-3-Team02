package com.back.domain.transactions.dto

data class CreateTransactionRequestDto(
    val assetId: Int,
    val type: String,
    val amount: Long,
    val content: String,
    val date: String
)
