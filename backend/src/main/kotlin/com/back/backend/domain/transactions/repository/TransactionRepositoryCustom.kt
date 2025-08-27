package com.back.backend.domain.transactions.repository

import com.back.backend.domain.transactions.entity.Transaction
import com.back.backend.domain.transactions.entity.TransactionType
import java.time.LocalDateTime

interface TransactionRepositoryCustom {
    fun findByAccountId(accountId: Int): List<Transaction>
    fun searchTransactions(
        type: TransactionType? = null,
        startDate: LocalDateTime? = null,
        endDate: LocalDateTime? = null,
        minAmount: Int? = null,
        maxAmount: Int? = null
    ): List<Transaction>
}