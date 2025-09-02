package com.back.domain.account.repository

import com.back.domain.account.entity.Account
import com.back.domain.member.entity.Member
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Account Repository 테스트")
class AccountRepositoryTest {

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var accountRepository: AccountRepository

    private lateinit var member: Member
    private lateinit var account1: Account
    private lateinit var account2: Account

    @BeforeEach
    fun setUp() {
        // 테스트용 Member 생성
        member = Member(
            email = "test@test.com",
            password = "password123",
            name = "테스트유저",
            phoneNumber = "010-1234-5678"
        )
        
        // 테스트용 Account 생성
        account1 = Account(
            member = member,
            name = "테스트계좌1",
            accountNumber = "123-456-789",
            balance = 10000L
        )
        
        account2 = Account(
            member = member,
            name = "테스트계좌2",
            accountNumber = "987-654-321",
            balance = 20000L
        )
    }

    @Test
    @DisplayName("계좌 저장 테스트")
    fun `계좌 저장 테스트`() {
        // given
        entityManager.persistAndFlush(member)

        // when
        val savedAccount = accountRepository.save(account1)

        // then
        assertNotNull(savedAccount.id)
        assertEquals(account1.name, savedAccount.name)
        assertEquals(account1.accountNumber, savedAccount.accountNumber)
        assertEquals(account1.balance, savedAccount.balance)
        assertEquals(member.id, savedAccount.member.id)
    }

    @Test
    @DisplayName("회원별 계좌 목록 조회 테스트")
    fun `회원별 계좌 목록 조회 테스트`() {
        // given
        entityManager.persistAndFlush(member)
        entityManager.persistAndFlush(account1)
        entityManager.persistAndFlush(account2)

        // when
        val accounts = accountRepository.findAllByMemberId(member.id)

        // then
        assertEquals(2, accounts.size)
        assertTrue(accounts.any { it.name == "테스트계좌1" })
        assertTrue(accounts.any { it.name == "테스트계좌2" })
    }

    @Test
    @DisplayName("회원별 삭제되지 않은 계좌 목록 조회 테스트")
    fun `회원별 삭제되지 않은 계좌 목록 조회 테스트`() {
        // given
        entityManager.persistAndFlush(member)
        entityManager.persistAndFlush(account1)
        
        // account2를 삭제 상태로 설정
        account2.isDeleted = true
        entityManager.persistAndFlush(account2)

        // when
        val activeAccounts = accountRepository.findAllByMemberIdAndIsDeletedFalse(member.id)

        // then
        assertEquals(1, activeAccounts.size)
        assertEquals("테스트계좌1", activeAccounts[0].name)
        assertFalse(activeAccounts[0].isDeleted)
    }

    @Test
    @DisplayName("계좌번호와 이름으로 계좌 존재 확인 테스트 - 존재하는 경우")
    fun `계좌번호와 이름으로 계좌 존재 확인 테스트 - 존재하는 경우`() {
        // given
        entityManager.persistAndFlush(member)
        entityManager.persistAndFlush(account1)

        // when
        val exists = accountRepository.existsAccountByAccountNumberAndName(
            account1.accountNumber, 
            account1.name
        )

        // then
        assertTrue(exists)
    }

    @Test
    @DisplayName("계좌번호와 이름으로 계좌 존재 확인 테스트 - 존재하지 않는 경우")
    fun `계좌번호와 이름으로 계좌 존재 확인 테스트 - 존재하지 않는 경우`() {
        // given
        entityManager.persistAndFlush(member)
        entityManager.persistAndFlush(account1)

        // when
        val exists = accountRepository.existsAccountByAccountNumberAndName(
            "999-999-999", 
            "존재하지않는계좌"
        )

        // then
        assertFalse(exists)
    }

    @Test
    @DisplayName("계좌 ID로 계좌 조회 테스트")
    fun `계좌 ID로 계좌 조회 테스트`() {
        // given
        entityManager.persistAndFlush(member)
        val savedAccount = entityManager.persistAndFlush(account1)

        // when
        val foundAccount = accountRepository.findById(savedAccount.id!!)

        // then
        assertTrue(foundAccount.isPresent)
        assertEquals(account1.name, foundAccount.get().name)
        assertEquals(account1.accountNumber, foundAccount.get().accountNumber)
    }

    @Test
    @DisplayName("계좌 수정 테스트")
    fun `계좌 수정 테스트`() {
        // given
        entityManager.persistAndFlush(member)
        val savedAccount = entityManager.persistAndFlush(account1)
        
        val newName = "수정된계좌명"
        val newBalance = 50000L

        // when
        savedAccount.name = newName
        savedAccount.balance = newBalance
        val updatedAccount = accountRepository.save(savedAccount)

        // then
        assertEquals(newName, updatedAccount.name)
        assertEquals(newBalance, updatedAccount.balance)
    }

    @Test
    @DisplayName("계좌 삭제 테스트")
    fun `계좌 삭제 테스트`() {
        // given
        entityManager.persistAndFlush(member)
        val savedAccount = entityManager.persistAndFlush(account1)

        // when
        savedAccount.isDeleted = true
        val deletedAccount = accountRepository.save(savedAccount)

        // then
        assertTrue(deletedAccount.isDeleted)
    }

    @Test
    @DisplayName("다른 회원의 계좌는 조회되지 않음 테스트")
    fun `다른 회원의 계좌는 조회되지 않음 테스트`() {
        // given
        val otherMember = Member(
            email = "other@test.com",
            password = "password123",
            name = "다른유저",
            phoneNumber = "010-9876-5432"
        )
        
        entityManager.persistAndFlush(member)
        entityManager.persistAndFlush(otherMember)
        entityManager.persistAndFlush(account1)
        
        val otherAccount = Account(
            member = otherMember,
            name = "다른계좌",
            accountNumber = "111-222-333",
            balance = 30000L
        )
        entityManager.persistAndFlush(otherAccount)

        // when
        val memberAccounts = accountRepository.findAllByMemberId(member.id)
        val otherMemberAccounts = accountRepository.findAllByMemberId(otherMember.id)

        // then
        assertEquals(1, memberAccounts.size)
        assertEquals(1, otherMemberAccounts.size)
        assertEquals(member.id, memberAccounts[0].member.id)
        assertEquals(otherMember.id, otherMemberAccounts[0].member.id)
    }
}
