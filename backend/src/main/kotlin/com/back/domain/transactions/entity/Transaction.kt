package com.back.domain.transactions.entity

import com.back.global.jpa.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.time.LocalDateTime

@Entity
class Transaction (
//    @field:ManyToOne(fetch = FetchType.LAZY)
//    @field:JoinColumn(name = "asset_id", nullable = false)
//    @field:OnDelete(action = OnDeleteAction.CASCADE)
//    val asset : Asset,
    //Asset 도메인 완성 이후 코드로 변환
    @field:Enumerated(EnumType.STRING) var type : TransactionType,
    var amount: Long,
    var content: String,
    var date: LocalDateTime
) : BaseEntity()