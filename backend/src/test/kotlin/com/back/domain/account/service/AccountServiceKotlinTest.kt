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

@DisplayName("Account 서비스 Kotlin 테스트")
class AccountServiceKotlinTest {
    
    private lateinit var accountRepository: AccountRepository
    private lateinit var accountService: AccountService
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
        
        createDto = RqCreateAccountDto(
            name = "테스트계좌",
            accountNumber = "123-456-789",
            balance = 10000L
        )
        
        account = Account.create(createDto, member)
        // id는 BaseEntity에서 관리되므로 여기서 설정하지 않음
        
        updateDto = RqUpdateAccountDto(
            accountNumber = "987-654-321"
        )
    }
    
    @Test
    @DisplayName("기본 테스트 - 클래스 로딩 확인")
    fun basicTest() {
        assertTrue(true)
    }
    
    @Test
    @DisplayName("AccountService 생성 테스트")
    fun accountServiceCreationTest() {
        assertNotNull(accountService)
        assertNotNull(accountRepository)
    }

    @Test
    @DisplayName("계좌 생성 성공 테스트")
    fun createAccountSuccessTest() {
        // given
        `when`(accountRepository.existsAccountByAccountNumberAndName(
            createDto.accountNumber, 
            createDto.name
        )).thenReturn(false)
        `when`(accountRepository.save(any(Account::class.java))).thenReturn(account)

        // when
        val result = accountService.createAccount(createDto, member)

        // then
        assertNotNull(result)
        assertEquals(createDto.name, result.name)
        assertEquals(createDto.accountNumber, result.accountNumber)
        assertEquals(createDto.balance, result.balance)
        assertEquals(member, result.member)
        
        verify(accountRepository).existsAccountByAccountNumberAndName(
            createDto.accountNumber, 
            createDto.name
        )
        verify(accountRepository).save(any(Account::class.java))
    }

    @Test
    @DisplayName("계좌 생성 실패 테스트 - 중복된 계좌")
    fun createAccountFailureTest() {
        // given
        `when`(accountRepository.existsAccountByAccountNumberAndName(
            createDto.accountNumber, 
            createDto.name
        )).thenReturn(true)

        // when & then
        assertThrows<AccountDuplicateException> {
            accountService.createAccount(createDto, member)
        }
        
        verify(accountRepository).existsAccountByAccountNumberAndName(
            createDto.accountNumber, 
            createDto.name
        )
        verify(accountRepository, never()).save(any(Account::class.java))
    }

    @Test
    @DisplayName("회원별 계좌 목록 조회 테스트")
    fun getAccountsByMemberTest() {
        // given
        val accounts = listOf(account)
        `when`(accountRepository.findAllByMemberEmailAndIsDeletedFalse(member.email))
            .thenReturn(accounts)

        // when
        val result = accountService.getAccountsByMemberId(member)

        // then
        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals(account, result[0])
        
        verify(accountRepository).findAllByMemberEmailAndIsDeletedFalse(member.email)
    }

    @Test
    @DisplayName("특정 계좌 조회 성공 테스트")
    fun getAccountSuccessTest() {
        // given
        `when`(accountRepository.findById(1)).thenReturn(java.util.Optional.of(account))

        // when
        val result = accountService.getAccount(1, member)

        // then
        assertNotNull(result)
        assertEquals(account, result)
        
        verify(accountRepository).findById(1)
    }

    @Test
    @DisplayName("특정 계좌 조회 실패 테스트 - 계좌 없음")
    fun getAccountFailureTest() {
        // given
        `when`(accountRepository.findById(999)).thenReturn(java.util.Optional.empty())

        // when & then
        assertThrows<AccountNotFoundException> {
            accountService.getAccount(999, member)
        }
        
        verify(accountRepository).findById(999)
    }

    @Test
    @DisplayName("계좌 수정 성공 테스트")
    fun updateAccountSuccessTest() {
        // given
        `when`(accountRepository.findById(1)).thenReturn(java.util.Optional.of(account))

        // when
        accountService.updateAccount(1, member, updateDto)

        // then
        assertEquals(updateDto.accountNumber, account.accountNumber)
        verify(accountRepository).findById(1)
    }

    @Test
    @DisplayName("계좌 삭제 성공 테스트")
    fun deleteAccountSuccessTest() {
        // given
        `when`(accountRepository.findById(1)).thenReturn(java.util.Optional.of(account))
        assertFalse(account.isDeleted)

        // when
        accountService.deleteAccount(1, member)

        // then
        assertTrue(account.isDeleted)
        verify(accountRepository).findById(1)
    }
}
