package com.back.domain.transactions.service

import com.back.domain.account.entity.Account
import com.back.domain.account.repository.AccountRepository
import com.back.domain.member.entity.Member
import com.back.domain.member.repository.MemberRepository
import com.back.domain.transactions.dto.CreateAccTracRequestDto
import com.back.domain.transactions.entity.AccountTransaction
import com.back.domain.transactions.entity.TransactionType
import com.back.domain.transactions.repository.AccountTransactionRepository
import jakarta.transaction.Transactional
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountTransactionServiceTest (
    @Autowired private val accountTransactionRepository: AccountTransactionRepository,
    @Autowired private val accountTransactionService: AccountTransactionService,
    @Autowired private val accountRepository: AccountRepository,
    @Autowired private val memberRepository: MemberRepository
) {
    private lateinit var member: Member
    private lateinit var account: Account
    private lateinit var accTrans1 : AccountTransaction
    private lateinit var accTrans2 : AccountTransaction
    private lateinit var accTrans3 : AccountTransaction

    @BeforeAll
    fun setUp() {
        member = Member(
            "test@test.com",
            "password",
            "테스트",
            "010-1111-1111"
        )
        memberRepository.save(member)

        account = Account(
            member,
            "{계좌 번호}",
            10000L,
            "테스트 계좌"
        )
        accountRepository.save(account)

        accTrans1 = AccountTransaction(
            account,
            TransactionType.ADD,
            1000,
            "테스트 1",
            LocalDateTime.parse("2025-09-01T10:00:00")
        )
        accountTransactionRepository.save(accTrans1)

        accTrans2 = AccountTransaction(
            account,
            TransactionType.REMOVE,
            2000,
            "테스트 2",
            LocalDateTime.parse("2025-09-01T11:00:00")
        )
        accountTransactionRepository.save(accTrans2)

        accTrans3 = AccountTransaction(
            account,
            TransactionType.ADD,
            3000,
            "테스트 3",
            LocalDateTime.parse("2025-09-01T12:00:00")
        )
        accountTransactionRepository.save(accTrans3)
    }

    @Test
    @DisplayName("createAccountTransaction 테스트")
    fun createAccountTransaction() {
        val dto = CreateAccTracRequestDto(
            account.id,
            "ADD",
            1500,
            "추가 입금",
            "2025-09-01T13:00:00"
        )

        val saved = accountTransactionService.createAccountTransactions(dto, member)
        val found = accountTransactionService.findById(saved.id)

        assertEquals(saved, found)
    }

    @Test
    @DisplayName("findByAccountId 테스트")
    fun findByAccountIdTest() {
        val result = accountTransactionService.findByAccountId(account.id, member)
        assertEquals(3, result.size)
    }

    @Test
    @DisplayName("findAll 테스트")
    fun findAllTest() {
        val all = accountTransactionService.findAll()
        assertEquals(3, all.size)
    }

    @Test
    @DisplayName("findAccTransactionByAccountIds 테스트")
    fun findAccTransactionsByAccountIdsTest() {
        val result = accountTransactionService.findAccTransactionsByAccountIds(listOf(account.id))
        assertTrue { result.containsKey(account.id) }
        assertEquals(3, result[account.id]?.size)
    }
}