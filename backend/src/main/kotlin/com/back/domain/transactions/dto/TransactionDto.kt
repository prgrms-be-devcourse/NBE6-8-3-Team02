package com.back.domain.transactions.dto

import com.back.domain.transactions.entity.Transaction
import java.time.LocalDateTime

data class TransactionDto(
    val id: Int,
    val assetId: Int,
    val type: String,
    val amount: Long,
    val content: String,
    val date: LocalDateTime,
    val createdDate: LocalDateTime,
    val modifyDate: LocalDateTime
) {
    constructor(transaction: Transaction) : this(
        id = transaction.id,
        assetId = transaction.asset.id,
        type = transaction.type.name,
        amount = transaction.amount,
        content = transaction.content,
        date = transaction.date,
        createdDate = transaction.createDate,
        modifyDate = transaction.modifyDate,
    )
}
