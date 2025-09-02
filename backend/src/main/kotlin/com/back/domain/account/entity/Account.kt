package com.back.domain.account.entity

import com.back.domain.account.dto.RqCreateAccountDto
import com.back.domain.account.exception.AccountAccessDeniedException
import com.back.domain.account.exception.AccountNumberUnchangedException
import com.back.domain.member.entity.Member
import com.back.domain.transactions.entity.TransactionType
import com.back.global.jpa.entity.BaseEntity
import jakarta.persistence.*

@Entity
class Account(
    // JPA 관계 매핑: Member와의 관계
    // @ManyToOne: 여러 Account가 하나의 Member에 속함
    // fetch = FetchType.LAZY: 필요할 때만 Member 정보를 가져옴 (성능 최적화)
    // @JoinColumn: 외래키 컬럼명 지정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var member: Member,    
    var accountNumber: String,
    var balance: Long,
    var name: String,
    
    // 삭제 여부: Boolean 타입, 기본값 false
    // isDeleted = false: 매개변수에 기본값 지정
    var isDeleted: Boolean = false
) : BaseEntity() { 
    
    // JPA를 위한 기본 생성자 (매개변수 없음)
    // constructor(): 보조 생성자 선언
    // this(): 주 생성자 호출
    constructor() : this(
        member = Member(
            email = "",
            password = "",
            name = "",
            phoneNumber = ""
        ),        // 임시 Member 객체 생성
        accountNumber = "",       // 빈 문자열
        balance = 0L,            // 0 (L은 Long 타입 표시)
        name = "",               // 빈 문자열
        isDeleted = false        // false
    )

    // Java 버전과 동일한 companion object create 메서드
    companion object {
        fun create(rqCreateAccountDto: RqCreateAccountDto, member: Member): Account {
            return Account(
                member = member,
                name = rqCreateAccountDto.name,
                accountNumber = rqCreateAccountDto.accountNumber,
                balance = rqCreateAccountDto.balance,
                isDeleted = false
            )
        }
    }

    // 잔액 업데이트 메서드
    fun updateBalance(type: TransactionType, amount: Long): Account {
        when (type) {
            TransactionType.ADD -> {
                this.balance = this.balance + amount
            }
            TransactionType.REMOVE -> {
                if (this.balance < amount) {
                    throw IllegalArgumentException("잔액이 부족합니다.")
                }
                this.balance = this.balance - amount
            }
        }
        return this
    }

    // 소유자 검증 메서드
    fun validateOwner(member: Member) {
        if (this.member.email != member.email) {
            throw AccountAccessDeniedException()
        }
    }

    // 계좌번호 업데이트 메서드
    fun updateAccountNumber(newAccountNumber: String) {
        if (this.accountNumber == newAccountNumber) {
            throw AccountNumberUnchangedException()
        }
        this.accountNumber = newAccountNumber
    }

    // 계좌 삭제 메서드 (소프트 삭제)
    fun deleteAccount() {
        this.isDeleted = true
    }
}