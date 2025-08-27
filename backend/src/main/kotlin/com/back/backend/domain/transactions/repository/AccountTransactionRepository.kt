package com.back.backend.domain.transactions.repository

import com.back.backend.domain.transactions.entity.AccountTransaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountTransactionRepository : JpaRepository<AccountTransaction, Int> {
    fun findByAccountId(accountId: Int): List<AccountTransaction>
    fun findByAccountIdIn(accountIds: List<Int>): List<AccountTransaction>
}