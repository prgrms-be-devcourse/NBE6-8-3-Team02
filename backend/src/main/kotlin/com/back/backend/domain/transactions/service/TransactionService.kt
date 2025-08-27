package com.back.backend.domain.transactions.service

import com.back.backend.domain.transactions.dto.UpdateTransactionRequestDto
import com.back.backend.domain.transactions.entity.Transaction
import com.back.backend.domain.transactions.entity.TransactionType
import com.back.backend.domain.transactions.repository.TransactionRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TransactionService (
    private val transactionRepository: TransactionRepository
    //private val assetService: AssetService
    //private val accountService: AccountService
    // 둘다 각각 asset, account 도메인이 완료 되어야 함.
    // 추가로, 원래 프로젝트에선 repository로 선언되어 있었으나,
    // SRP 원칙에 맞게 서비스를 사용하도록 변경.
) {
    /*
    fun createTransaction(dto: CreateTransactionRequestDto): Transaction {
        val asset: Asset = assetService.findById(dto.assetId).orElseThrow {
            IllegalArgumentException("존재하지 않는 자산입니다.")
        }
        val transaction: Transaction = Transaction(
            asset = asset,
            type = TransactionType.valueOf(dto.type),
            amount = dto.amount,
            content = dto.content,
            date = LocalDateTime.parse(dto.date)
        )
        return transactionRepository.save(transaction)
    }
     */// Asset 도메인이 추가되어야 함.

    fun findAll() : List<Transaction> {
        return transactionRepository.findAll()
    }

    fun findById(id: Int) : Transaction {
        return transactionRepository.findById(id).orElse(null)
    }

    fun updateById(dto: UpdateTransactionRequestDto): Transaction {
        val transaction: Transaction = transactionRepository.findById(dto.id).orElseThrow {
            IllegalArgumentException("존재하지 않는 자산입니다.")
        }
        transaction.type = TransactionType.valueOf(dto.type)
        transaction.amount = dto.amount
        transaction.content = dto.content
        transaction.date = LocalDateTime.parse(dto.date)

        return transactionRepository.save(transaction)
    }

    /*
    fun findByAssetId(assetId: Int): List<Transaction> {
        assetService.findById(assetId).orElseThrow {
            IllegalArgumentException("존재하지 않는 자산입니다.")
        }
        return transactionRepository.findByAssetId(assetId)
    }
     */// Asset 도메인이 추가되어야 함.

    fun searchTransactions(
        type: String?,
        startDate: String?,
        endDate: String?,
        minAmount: Int?,
        maxAmount: Int?,
        ): List<Transaction> {

        val start: LocalDateTime? = startDate?.takeIf { it.isNotBlank() }?.let { LocalDateTime.parse(it) }
        val end: LocalDateTime? = endDate?.takeIf { it.isNotBlank() }?.let { LocalDateTime.parse(it) }

        val transactionType: TransactionType? = type?.takeIf { it.isNotBlank() }?.let {
            try {
                TransactionType.valueOf(it)
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException("유효하지 않은 거래 유형입니다: $it")
            }
        }

        return transactionRepository.searchTransactions(
            type = transactionType,
            startDate = start,
            endDate = end,
            minAmount = minAmount,
            maxAmount = maxAmount
        )
    }

    /*
    fun findTransactionsByAssetId(assetIds: List<Int>): List<Transaction> {
        val allTransactions: List<Transaction> = transactionRepository.findByAssetIdIn(assetIds)
        return allTransactions
            .groupBy { it.asset.id }
            .mapValues { entry -> entry.value.map { TransactionDto(it) } }
    }
     */// Asset 도메인이 추가되어야 함.
}
