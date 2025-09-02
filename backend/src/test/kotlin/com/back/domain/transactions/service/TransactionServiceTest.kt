package com.back.domain.transactions.service

import com.back.domain.asset.entity.Asset
import com.back.domain.asset.repository.AssetRepository
import com.back.domain.member.entity.Member
import com.back.domain.member.repository.MemberRepository
import com.back.domain.transactions.dto.CreateTransactionRequestDto
import com.back.domain.transactions.dto.UpdateTransactionRequestDto
import com.back.domain.transactions.entity.Transaction
import com.back.domain.transactions.entity.TransactionType
import com.back.domain.transactions.repository.TransactionRepository
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import kotlin.test.assertEquals

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionServiceTest (
    @Autowired private val transactionRepository: TransactionRepository,
    @Autowired private val transactionService: TransactionService,
    @Autowired private val assetRepository: AssetRepository,
    @Autowired private val membersRepository: MemberRepository,
) {
    private lateinit var member: Member
    private lateinit var asset: Asset
    private lateinit var transaction1: Transaction
    private lateinit var transaction2: Transaction
    private lateinit var transaction3: Transaction

    @BeforeAll
    fun setUp() {
        member = membersRepository.findByEmail("usertest@test.com")!!
        asset = assetRepository.findAllByMemberId(member.id)[0]

        transaction1 = Transaction(
            asset,
            TransactionType.ADD,
            1000,
            "테스트 1",
            LocalDateTime.parse("2025-09-01T10:00:00")
        )
        transaction2 = Transaction(
            asset,
            TransactionType.REMOVE,
            2000,
            "테스트 2",
            LocalDateTime.parse("2025-09-01T10:00:00")
        )
        transaction3 = Transaction(
            asset,
            TransactionType.ADD,
            3000,
            "테스트 3",
            LocalDateTime.parse("2025-09-01T12:00:00")
        )

        transactionRepository.save(transaction1)
        transactionRepository.save(transaction2)
        transactionRepository.save(transaction3)
    }

    @Test
    @DisplayName("createTransaction 테스트")
    fun createTransactionTest() {
        val dto = CreateTransactionRequestDto(
            asset.id,
            "ADD",
            1000,
            "테스트 입금",
            "2025-09-01T10:00:00"
        )

        val saved = transactionService.createTransaction(dto)
        val found = transactionService.findById(saved.id)

        assertEquals(saved, found)
    }

    @Test
    @DisplayName("findAll 테스트")
    fun findAllTest() {
        val transactions = transactionRepository.findAll()
        assertEquals(transactionRepository.count().toInt(), transactions.size)
    }

    @Test
    @DisplayName("findById 테스트")
    fun findByIdTest() {
        val result1 = transactionService.findById(1)
        val result2 = transactionService.findById(2)
        val result3 = transactionService.findById(3)

        assertEquals(transaction1, result1)
        assertEquals(transaction2, result2)
        assertEquals(transaction3, result3)
    }

    @Test
    @DisplayName("updateById 테스트")
    fun updateByIdTest() {
        val dto = UpdateTransactionRequestDto(
            1,
            "REMOVE",
            2000,
            "업데이트",
            "2025-09-01T12:30:00"
        )

        assertEquals(transaction1, transactionService.findById(1))
        val update = transactionService.updateById(dto)
        assertEquals(transaction1, update)
    }

    @Test
    @DisplayName("findByAssetId 테스트")
    fun findByAssetIdTest() {
        val result = transactionService.findByAssetId(1)
        assertEquals(transactionRepository.count().toInt(), result.size)
    }

    @Test
    @DisplayName("searchTransactions 테스트")
    fun searchTransactionsTest() {
        val result = transactionService.searchTransactions(
            type = "ADD",
            startDate = "2025-09-01T10:00:00",
            endDate = "2025-09-01T20:00:00",
            minAmount = 1000,
            maxAmount = 5000
        )
        assertEquals(2, result.size)
    }

    @Test
    @DisplayName("findTransactionsByAssetId 테스트")
    fun findTransactionsByAssetIdTest() {
        val result = transactionService.findTransactionsByAssetId(listOf(1))
        assertTrue(result.containsKey(1))
    }

}