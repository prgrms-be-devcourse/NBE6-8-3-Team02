package com.back.domain.account.repository

import com.back.domain.account.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : JpaRepository<Account, Int> {
    
    // 특정 회원의 모든 계좌를 찾는 메서드
    // Spring Data JPA가 메서드 이름을 분석해서 자동으로 쿼리 생성
    // findAllByMemberId → SELECT * FROM account WHERE member_id = ?
    fun findAllByMemberId(memberId: Int): List<Account>
    
    // 특정 회원의 삭제되지 않은 계좌만 찾는 메서드
    // findAllByMemberIdAndIsDeletedFalse → SELECT * FROM account WHERE member_id = ? AND is_deleted = false
    fun findAllByMemberIdAndIsDeletedFalse(memberId: Int): List<Account>
    
    // 특정 회원의 삭제되지 않은 계좌만 찾는 메서드 (email로 검색)
    // findAllByMemberEmailAndIsDeletedFalse → SELECT * FROM account WHERE member_email = ? AND is_deleted = false
    fun findAllByMemberEmailAndIsDeletedFalse(memberEmail: String): List<Account>
    
    // 계좌번호와 이름으로 계좌가 존재하는지 확인하는 메서드
    // existsAccountByAccountNumberAndName → SELECT COUNT(*) > 0 FROM account WHERE account_number = ? AND name = ?
    fun existsAccountByAccountNumberAndName(accountNumber: String, name: String): Boolean
}

