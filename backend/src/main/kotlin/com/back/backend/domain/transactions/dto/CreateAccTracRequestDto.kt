package com.back.backend.domain.transactions.dto

data class CreateAccTracRequestDto(
    val assetId: Int,
    val type: String,
    val amount: Long,
    val content: String,
    val date: String
)
