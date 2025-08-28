package com.back.domain.transactions.entity

import com.back.global.jpa.entity.BaseEntity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.time.LocalDateTime

class AccountTransaction (
//    @field:ManyToOne(fetch = FetchType.LAZY)
//    @field:JoinColumn(name = "asset_id", nullable = false)
//    @field:OnDelete(action = OnDeleteAction.CASCADE)
//    val account: Account,
    //Account 도메인 생성 이후 코드로 변환
    @Enumerated(EnumType.STRING) var type: TransactionType,
    var amount: Long,
    var content: String,
    var date: LocalDateTime
) : BaseEntity()