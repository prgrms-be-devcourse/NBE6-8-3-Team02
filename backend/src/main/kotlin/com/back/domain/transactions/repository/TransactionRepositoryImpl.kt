package com.back.domain.transactions.repository

import com.back.backend.domain.transactions.entity.QTransaction
import com.back.domain.transactions.entity.Transaction
import com.back.domain.transactions.entity.TransactionType
import com.querydsl.core.BooleanBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class TransactionRepositoryImpl (
    private val queryFactory: JPAQueryFactory
) : TransactionRepositoryCustom {


    override fun findByAccountId(accountId: Int): List<Transaction> {
        val transaction = QTransaction.transaction
        //val asset = QAsset.asset
        //Asset 엔티티가 추가되어야 함.

        /*
        return queryFactory
            .selectFrom(transaction)
            .join(transaction.asset, asset)
            .where(asset.member.id.eq(accountId))
            .fetch()
         */
        return findByAccountId(accountId) //임시 값, 나중에 위의 Asset이 구현 된 이후 위의 주석 값으로 대체.
    }


    override fun searchTransactions(
        type: TransactionType?,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        minAmount: Int?,
        maxAmount: Int?
    ): List<Transaction> {
        val transaction = QTransaction.transaction
        val builder = BooleanBuilder()

        type?.let { builder.and(transaction.type.eq(it)) }
        startDate?.let { builder.and(transaction.date.goe(it)) }
        endDate?.let { builder.and(transaction.date.loe(it)) }
        minAmount?.let { builder.and(transaction.amount.goe(it)) }
        maxAmount?.let { builder.and(transaction.amount.loe(it)) }

        return queryFactory
            .selectFrom(transaction)
            .where(builder)
            .orderBy(transaction.date.desc())
            .fetch()
    }
}