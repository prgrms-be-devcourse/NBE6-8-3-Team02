package com.back.domain.account.service

import com.back.domain.account.dto.RqCreateAccountDto
import com.back.domain.account.dto.RqUpdateAccountDto
import com.back.domain.account.entity.Account
import com.back.domain.account.exception.AccountDuplicateException
import com.back.domain.account.exception.AccountNotFoundException
import com.back.domain.account.repository.AccountRepository
import com.back.domain.member.entity.Member
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService(
    // 생성자 주입: Kotlin에서는 생성자 매개변수가 자동으로 필드가 됨
    private val accountRepository: AccountRepository
) {

    // 계좌 중복 확인 메서드
    // private: 클래스 내부에서만 사용
    // Unit: Java의 void와 동일 (반환값 없음)
    private fun checkAccountDuplicate(rqCreateAccountDto: RqCreateAccountDto) {
        // Repository에서 계좌번호와 이름으로 중복 확인
        if (accountRepository.existsAccountByAccountNumberAndName(
                rqCreateAccountDto.accountNumber,  // Kotlin에서는 .으로 접근
                rqCreateAccountDto.name
            )
        ) {
            throw AccountDuplicateException()  // Kotlin에서는 new 키워드 불필요
        }
    }

    // 계좌 ID로 계좌 찾기 (내부 메서드)
    private fun findByAccount(accountId: Int): Account {
        // findById는 Optional을 반환하므로 orElseThrow 사용
        return accountRepository.findById(accountId).orElseThrow {
            throw AccountNotFoundException()
        }
    }

    // 계좌 생성 메서드
    // public: 기본적으로 public (생략 가능)
    fun createAccount(rqCreateAccountDto: RqCreateAccountDto, member: Member): Account {
        // 1. 중복 확인
        checkAccountDuplicate(rqCreateAccountDto)
        
        // 2. Account 엔티티 생성 (companion object의 create 메서드 사용)
        val account = Account.create(rqCreateAccountDto, member)
        
        // 3. 데이터베이스에 저장하고 반환
        return accountRepository.save(account)
    }

    // 회원별 계좌 목록 조회
    fun getAccountsByMemberId(member: Member): List<Account> {
        // 삭제되지 않은 계좌만 조회 (member.email 사용)
        return accountRepository.findAllByMemberEmailAndIsDeletedFalse(member.email)
    }

    // 특정 계좌 조회 (소유자 검증 포함)
    fun getAccount(accountId: Int, member: Member): Account {
        // 1. 계좌 찾기
        val account = findByAccount(accountId)
        
        // 2. 소유자 검증 (Account 엔티티의 메서드 호출)
        account.validateOwner(member)
        
        return account
    }

    // 계좌 수정 (트랜잭션 적용)
    @Transactional
    fun updateAccount(accountId: Int, member: Member, rqUpdateAccountDto: RqUpdateAccountDto) {
        // 1. 계좌 조회 및 소유자 검증
        val account = getAccount(accountId, member)
        
        // 2. 계좌번호 업데이트
        account.updateAccountNumber(rqUpdateAccountDto.accountNumber)
        
        // 3. 변경사항 자동 저장 (JPA의 dirty checking)
    }

    // 계좌 삭제 (트랜잭션 적용)
    @Transactional
    fun deleteAccount(accountId: Int, member: Member) {
        // 1. 계좌 조회 및 소유자 검증
        val account = getAccount(accountId, member)
        
        // 2. 계좌 삭제 처리 (실제 삭제가 아닌 isDeleted = true)
        account.deleteAccount()
        
        // 3. 변경사항 자동 저장 (JPA의 dirty checking)
    }
}

