package com.back.domain.account.dto

// data class: Kotlin에서 DTO를 만들 때 사용하는 특별한 클래스
// 자동으로 equals(), hashCode(), toString(), copy() 메서드 생성
// Java의 @Getter와 동일한 기능
data class RqUpdateAccountDto(
    val accountNumber: String  // 수정할 계좌번호
)

