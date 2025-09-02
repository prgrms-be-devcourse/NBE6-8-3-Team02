package com.back.domain.account.entity

import com.back.domain.account.dto.RqCreateAccountDto
import com.back.domain.account.exception.AccountAccessDeniedException
import com.back.domain.account.exception.AccountNumberUnchangedException
import com.back.domain.member.entity.Member
import com.back.domain.transactions.entity.TransactionType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@DisplayName("Account 엔티티 Kotlin 테스트")
class AccountKotlinTest {

    private lateinit var member: Member
    private lateinit var account: Account
    private lateinit var createDto: RqCreateAccountDto

    @BeforeEach
    fun setUp() {
        member = Member(
            email = "test@test.com",
            password = "password123",
            name = "테스트유저",
            phoneNumber = "010-1234-5678"
        )
        
        createDto = RqCreateAccountDto(
            name = "테스트계좌",
            accountNumber = "123-456-789",
            balance = 10000L
        )
        
        account = Account.create(createDto, member)
    }

    @Test
    @DisplayName("Account 생성 테스트")
    fun `Account 생성 테스트`() {
        // given & when
        val newAccount = Account.create(createDto, member)

        // then
        assertNotNull(newAccount)
        assertEquals(createDto.name, newAccount.name)
        assertEquals(createDto.accountNumber, newAccount.accountNumber)
        assertEquals(createDto.balance, newAccount.balance)
        assertEquals(member, newAccount.member)
        assertFalse(newAccount.isDeleted)
    }

    @Test
    @DisplayName("잔액 추가 테스트")
    fun `잔액 추가 테스트`() {
        // given
        val initialBalance = account.balance
        val addAmount = 5000L

        // when
        account.updateBalance(TransactionType.ADD, addAmount)

        // then
        assertEquals(initialBalance + addAmount, account.balance)
    }

    @Test
    @DisplayName("잔액 차감 테스트")
    fun `잔액 차감 테스트`() {
        // given
        val initialBalance = account.balance
        val removeAmount = 3000L

        // when
        account.updateBalance(TransactionType.REMOVE, removeAmount)

        // then
        assertEquals(initialBalance - removeAmount, account.balance)
    }

    @Test
    @DisplayName("잔액 부족시 차감 실패 테스트")
    fun `잔액 부족시 차감 실패 테스트`() {
        // given
        val removeAmount = 20000L

        // when & then
        assertThrows<IllegalArgumentException> {
            account.updateBalance(TransactionType.REMOVE, removeAmount)
        }
    }

    @Test
    @DisplayName("소유자 검증 성공 테스트")
    fun `소유자 검증 성공 테스트`() {
        // given & when & then
        assertDoesNotThrow {
            account.validateOwner(member)
        }
    }

    @Test
    @DisplayName("소유자 검증 실패 테스트")
    fun `소유자 검증 실패 테스트`() {
        // given
        val otherMember = Member(
            email = "other@test.com",
            password = "password123",
            name = "다른유저",
            phoneNumber = "010-9876-5432"
        )

        // when & then
        assertThrows<AccountAccessDeniedException> {
            account.validateOwner(otherMember)
        }
    }

    @Test
    @DisplayName("계좌번호 업데이트 성공 테스트")
    fun `계좌번호 업데이트 성공 테스트`() {
        // given
        val newAccountNumber = "987-654-321"

        // when
        account.updateAccountNumber(newAccountNumber)

        // then
        assertEquals(newAccountNumber, account.accountNumber)
    }

    @Test
    @DisplayName("계좌번호 업데이트 실패 테스트 - 동일한 번호")
    fun `계좌번호 업데이트 실패 테스트 - 동일한 번호`() {
        // given
        val sameAccountNumber = account.accountNumber

        // when & then
        assertThrows<AccountNumberUnchangedException> {
            account.updateAccountNumber(sameAccountNumber)
        }
    }

    @Test
    @DisplayName("계좌 삭제 테스트")
    fun `계좌 삭제 테스트`() {
        // given
        assertFalse(account.isDeleted)

        // when
        account.deleteAccount()

        // then
        assertTrue(account.isDeleted)
    }

    @Test
    @DisplayName("기본 생성자 테스트")
    fun `기본 생성자 테스트`() {
        // when
        val emptyAccount = Account()

        // then
        assertNotNull(emptyAccount)
        assertEquals("", emptyAccount.name)
        assertEquals("", emptyAccount.accountNumber)
        assertEquals(0L, emptyAccount.balance)
        assertFalse(emptyAccount.isDeleted)
    }
}
