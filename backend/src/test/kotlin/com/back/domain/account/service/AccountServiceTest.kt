package com.back.domain.account.service

import com.back.domain.account.dto.RqCreateAccountDto
import com.back.domain.account.dto.RqUpdateAccountDto
import com.back.domain.account.entity.Account
import com.back.domain.account.exception.AccountDuplicateException
import com.back.domain.account.exception.AccountNotFoundException
import com.back.domain.account.repository.AccountRepository
import com.back.domain.member.entity.Member
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*

@DisplayName("Account 서비스 테스트")
class AccountServiceTest {

    private lateinit var accountService: AccountService
    private lateinit var accountRepository: AccountRepository
    private lateinit var member: Member
    private lateinit var account: Account
    private lateinit var createDto: RqCreateAccountDto
    private lateinit var updateDto: RqUpdateAccountDto

    @BeforeEach
    fun setUp() {
        accountRepository = mock(AccountRepository::class.java)
        accountService = AccountService(accountRepository)
        
        member = Member(
            email = "test@test.com",
            password = "password123",
            name = "테스트유저",
            phoneNumber = "010-1234-5678"
        )
        
        account = Account(
            member = member,
            name = "테스트계좌",
            accountNumber = "123-456-789",
            balance = 10000L
        )
        
        createDto = RqCreateAccountDto(
            name = "테스트계좌",
            accountNumber = "123-456-789",
            balance = 10000L
        )
        
        updateDto = RqUpdateAccountDto(
            accountNumber = "987-654-321"
        )
    }

    @Test
    @DisplayName("계좌 생성 성공 테스트")
    fun `계좌 생성 성공 테스트`() {
        // given
        `when`(accountRepository.existsAccountByAccountNumberAndName(any(), any())).thenReturn(false)
        `when`(accountRepository.save(any())).thenReturn(account)

        // when
        val result = accountService.createAccount(createDto, member)

        // then
        assertNotNull(result)
        assertEquals(account.name, result.name)
        assertEquals(account.accountNumber, result.accountNumber)
        assertEquals(account.balance, result.balance)
        assertEquals(account.member, result.member)
        
        verify(accountRepository).existsAccountByAccountNumberAndName(createDto.accountNumber, createDto.name)
        verify(accountRepository).save(any())
    }

    @Test
    @DisplayName("계좌 생성 실패 테스트 - 중복된 계좌")
    fun `계좌 생성 실패 테스트 - 중복된 계좌`() {
        // given
        `when`(accountRepository.existsAccountByAccountNumberAndName(any(), any())).thenReturn(true)

        // when & then
        assertThrows<AccountDuplicateException> {
            accountService.createAccount(createDto, member)
        }
        
        verify(accountRepository).existsAccountByAccountNumberAndName(createDto.accountNumber, createDto.name)
        verify(accountRepository, never()).save(any())
    }

    @Test
    @DisplayName("회원별 계좌 목록 조회 테스트")
    fun `회원별 계좌 목록 조회 테스트`() {
        // given
        val accounts = listOf(account)
        `when`(accountRepository.findAllByMemberIdAndIsDeletedFalse(member.id)).thenReturn(accounts)

        // when
        val result = accountService.getAccountsByMemberId(member)

        // then
        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals(account, result[0])
        
        verify(accountRepository).findAllByMemberIdAndIsDeletedFalse(member.id)
    }

    @Test
    @DisplayName("특정 계좌 조회 성공 테스트")
    fun `특정 계좌 조회 성공 테스트`() {
        // given
        val accountId = 1
        `when`(accountRepository.findById(accountId)).thenReturn(java.util.Optional.of(account))

        // when
        val result = accountService.getAccount(accountId, member)

        // then
        assertNotNull(result)
        assertEquals(account, result)
        
        verify(accountRepository).findById(accountId)
    }

    @Test
    @DisplayName("특정 계좌 조회 실패 테스트 - 계좌 없음")
    fun `특정 계좌 조회 실패 테스트 - 계좌 없음`() {
        // given
        val accountId = 999
        `when`(accountRepository.findById(accountId)).thenReturn(java.util.Optional.empty())

        // when & then
        assertThrows<AccountNotFoundException> {
            accountService.getAccount(accountId, member)
        }
        
        verify(accountRepository).findById(accountId)
    }

    @Test
    @DisplayName("계좌 수정 성공 테스트")
    fun `계좌 수정 성공 테스트`() {
        // given
        val accountId = 1
        `when`(accountRepository.findById(accountId)).thenReturn(java.util.Optional.of(account))

        // when
        accountService.updateAccount(accountId, member, updateDto)

        // then
        verify(accountRepository).findById(accountId)
        // updateAccountNumber 메서드가 호출되었는지 확인
        assertEquals(updateDto.accountNumber, account.accountNumber)
    }

    @Test
    @DisplayName("계좌 수정 실패 테스트 - 계좌 없음")
    fun `계좌 수정 실패 테스트 - 계좌 없음`() {
        // given
        val accountId = 999
        `when`(accountRepository.findById(accountId)).thenReturn(java.util.Optional.empty())

        // when & then
        assertThrows<AccountNotFoundException> {
            accountService.updateAccount(accountId, member, updateDto)
        }
        
        verify(accountRepository).findById(accountId)
    }

    @Test
    @DisplayName("계좌 삭제 성공 테스트")
    fun `계좌 삭제 성공 테스트`() {
        // given
        val accountId = 1
        `when`(accountRepository.findById(accountId)).thenReturn(java.util.Optional.of(account))

        // when
        accountService.deleteAccount(accountId, member)

        // then
        verify(accountRepository).findById(accountId)
        assertTrue(account.isDeleted)
    }

    @Test
    @DisplayName("계좌 삭제 실패 테스트 - 계좌 없음")
    fun `계좌 삭제 실패 테스트 - 계좌 없음`() {
        // given
        val accountId = 999
        `when`(accountRepository.findById(accountId)).thenReturn(java.util.Optional.empty())

        // when & then
        assertThrows<AccountNotFoundException> {
            accountService.deleteAccount(accountId, member)
        }
        
        verify(accountRepository).findById(accountId)
    }

    @Test
    @DisplayName("계좌 수정 실패 테스트 - 소유자가 아닌 경우")
    fun `계좌 수정 실패 테스트 - 소유자가 아닌 경우`() {
        // given
        val accountId = 1
        val otherMember = Member(
            email = "other@test.com",
            password = "password123",
            name = "다른유저",
            phoneNumber = "010-9876-5432"
        )
        
        `when`(accountRepository.findById(accountId)).thenReturn(java.util.Optional.of(account))

        // when & then
        assertThrows<Exception> {
            accountService.updateAccount(accountId, otherMember, updateDto)
        }
        
        verify(accountRepository).findById(accountId)
    }
}
