package com.back.domain.account.service

import com.back.domain.account.dto.RqCreateAccountDto
import com.back.domain.account.dto.RqUpdateAccountDto
import com.back.domain.account.entity.Account
import com.back.domain.account.exception.AccountDuplicateException
import com.back.domain.account.exception.AccountNotFoundException
import com.back.domain.account.repository.AccountRepository
import com.back.domain.member.entity.Member
import com.back.domain.member.entity.MemberRole
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import java.lang.reflect.Field

@DisplayName("Account 서비스 Kotlin 테스트")
class AccountServiceKotlinTest {
    
    @Test
    @DisplayName("기본 테스트 - 클래스 로딩 확인")
    fun `기본 테스트`() {
        println("기본 테스트 실행")
        assertTrue(true)
    }

    private lateinit var accountService: AccountService
    private lateinit var accountRepository: AccountRepository
    private lateinit var member: Member
    private lateinit var account: Account
    private lateinit var createDto: RqCreateAccountDto
    private lateinit var updateDto: RqUpdateAccountDto

    @BeforeEach
    fun setUp() {
        println("=== setUp 시작 ===")
        
        // 1단계: Repository만 생성
        accountRepository = mock(AccountRepository::class.java)
        println("1단계: accountRepository 생성 완료")
        
        // 2단계: Member 생성
        member = Member(
            email = "test@test.com",
            password = "password123",
            name = "테스트유저",
            phoneNumber = "010-1234-5678",
            role = MemberRole.USER
        )
        println("2단계: member 생성 완료")
        
        // 3단계: Account 생성
        account = Account(
            member = member,
            name = "테스트계좌",
            accountNumber = "123-456-789",
            balance = 10000L
        )
        println("3단계: account 생성 완료")
        
        // 4단계: DTO 생성
        createDto = RqCreateAccountDto(
            name = "테스트계좌",
            accountNumber = "123-456-789",
            balance = 10000L
        )
        println("4단계: createDto 생성 완료")
        
        updateDto = RqUpdateAccountDto(
            accountNumber = "987-654-321"
        )
        println("5단계: updateDto 생성 완료")
        
        // 6단계: AccountService 생성
        accountService = AccountService(accountRepository)
        println("6단계: accountService 생성 완료")
        
        // 7단계: Repository 모킹
        `when`(accountRepository.findAllByMemberEmailAndIsDeletedFalse(any())).thenReturn(listOf(account))
        println("7단계: Repository 모킹 완료")
        
        println("=== setUp 완료 ===")
    }

    @Test
    @DisplayName("계좌 생성 성공 테스트")
    fun `계좌 생성 성공 테스트`() {
        // given
        println("=== 디버깅 정보 ===")
        println("member: $member")
        println("member.email: ${member.email}")
        println("createDto: $createDto")
        println("account: $account")
        
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
        // member.email로 모킹
        `when`(accountRepository.findAllByMemberEmailAndIsDeletedFalse(any())).thenReturn(accounts)

        // when
        val result = accountService.getAccountsByMemberId(member)

        // then
        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals(account, result[0])
        
        verify(accountRepository).findAllByMemberEmailAndIsDeletedFalse(any())
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
            phoneNumber = "010-9876-5432",
            role = MemberRole.USER
        )
        
        `when`(accountRepository.findById(accountId)).thenReturn(java.util.Optional.of(account))

        // when & then
        assertThrows<Exception> {
            accountService.updateAccount(accountId, otherMember, updateDto)
        }
        
        verify(accountRepository).findById(accountId)
    }
}
