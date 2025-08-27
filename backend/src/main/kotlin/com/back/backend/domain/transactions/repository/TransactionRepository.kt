package com.back.backend.domain.transactions.repository

import com.back.backend.domain.transactions.entity.Transaction

interface TransactionRepository {
    fun findByAssetId(assetId: Int): List<Transaction>
    fun findByAssetIdIn(assetIds: List<Int>): List<Transaction>


}