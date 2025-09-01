package com.back.domain.transactions.repository

import com.back.domain.transactions.entity.Transaction
import com.back.domain.transactions.entity.TransactionType
import java.time.LocalDateTime

interface TransactionRepositoryCustom {
    fun findByAccountId(accountId: Int): List<Transaction>
    fun searchTransactions(
        type: TransactionType,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        minAmount: Int,
        maxAmount: Int
    ): List<Transaction>
}