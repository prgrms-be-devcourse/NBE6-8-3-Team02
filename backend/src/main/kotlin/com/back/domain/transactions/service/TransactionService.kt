package com.back.domain.transactions.service

import com.back.domain.asset.service.AssetService
import com.back.domain.transactions.dto.CreateTransactionRequestDto
import com.back.domain.transactions.dto.TransactionDto
import com.back.domain.transactions.dto.UpdateTransactionRequestDto
import com.back.domain.transactions.entity.Transaction
import com.back.domain.transactions.entity.TransactionType
import com.back.domain.transactions.repository.TransactionRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TransactionService (
    private val transactionRepository: TransactionRepository,
    private val assetService: AssetService
    //private val accountService: AccountService
    // 둘다 각각 asset, account 도메인이 완료 되어야 함.
    // 추가로, 원래 프로젝트에선 repository로 선언되어 있었으나,
    // SRP 원칙에 맞게 서비스를 사용하도록 변경.
) {
    //이러한 DTO를 인자를 받을 경우, 코드 간의 강결합이 생겨 문제가 발생 가능.
    //인자를 쪼개서 받거나, global-interface로 인자를 받아 결합 문제를 해결하는 것이 좋다.
    fun createTransaction(dto: CreateTransactionRequestDto): Transaction {
        val asset = requireNotNull(assetService.findById(dto.assetId)) {
            throw IllegalArgumentException("존재하지 않는 자산입니다.")
        }

        val transaction = Transaction(
            asset,
            TransactionType.valueOf(dto.type),
            dto.amount,
            dto.content,
            LocalDateTime.parse(dto.date)
        )
        return transactionRepository.save(transaction)
    }

    fun findAll() : List<Transaction> {
        return transactionRepository.findAll()
    }

    fun findById(id: Int) : Transaction {
        return transactionRepository.findById(id)
            .orElseThrow { IllegalArgumentException("존재하지 않는 거래입니다.") }
    }

    fun updateById(dto: UpdateTransactionRequestDto): Transaction {
        val transaction = transactionRepository.findById(dto.id).orElseThrow {
            IllegalArgumentException("존재하지 않는 자산입니다.")
        }
        transaction.type = TransactionType.valueOf(dto.type)
        transaction.amount = dto.amount
        transaction.content = dto.content
        transaction.date = LocalDateTime.parse(dto.date)

        return transactionRepository.save(transaction)
    }

    fun findByAssetId(assetId: Int): List<Transaction> {
        requireNotNull(assetService.findById(assetId)) {
            throw IllegalArgumentException("존재하지 않는 자산입니다.")
        }
        return transactionRepository.findByAssetId(assetId)
    }

    fun searchTransactions(
        //이게 정말 nullable 할 필요가 있을까? 고민 한번 해봐야 함.
        type: String,
        startDate: String,
        endDate: String,
        minAmount: Int,
        maxAmount: Int
        ): List<Transaction> {

        require(startDate.isNotBlank()) { "시작일은 공백일 수 없습니다." }
        val start = LocalDateTime.parse(startDate)

        require(endDate.isNotBlank()) { "종료일은 공백일 수 없습니다." }
        val end = LocalDateTime.parse(endDate)

        require(type.isNotBlank()) { "거래 유형은 공백일 수 없습니다." }
        val transactionType = let {
            try {
                TransactionType.valueOf(type.uppercase())
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException("유효하지 않은 거래 유형입니다: $it")
            }
        }

        return transactionRepository.searchTransactions(
            transactionType,
           start,
            end,
            minAmount,
            maxAmount
        )
    }

    fun findTransactionsByAssetId(assetIds: List<Int>): Map<Int, List<TransactionDto>> {
        val allTransactions: List<Transaction> = transactionRepository.findByAssetIdIn(assetIds)
        return allTransactions
            .groupBy { it.asset.id }
            .mapValues { entry -> entry.value.map { TransactionDto(it) } }
    }
}
