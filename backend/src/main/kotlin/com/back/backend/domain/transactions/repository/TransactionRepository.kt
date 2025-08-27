package com.back.backend.domain.transactions.repository

import com.back.backend.domain.transactions.entity.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository : JpaRepository<Transaction, Int>, TransactionRepositoryCustom {
    fun findByAssetId(assetId: Int): List<Transaction>
    fun findByAssetIdIn(assetIds: List<Int>): List<Transaction>
}