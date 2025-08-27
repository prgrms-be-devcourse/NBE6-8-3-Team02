package com.back.backend.domain.transactions.service

import com.back.backend.domain.transactions.entity.Transaction
import com.back.backend.domain.transactions.repository.TransactionRepository
import org.springframework.stereotype.Service

@Service
class TransactionService (
    private val transactionRepository: TransactionRepository
) {
    fun createTransaction(transaction: Transaction): Transaction {

    }
}
