package com.back.domain.transactions.entity

import com.back.domain.account.Account
import com.back.global.jpa.entity.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDateTime

@Entity
class AccountTransaction (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    val account: Account,
    //Account 도메인 생성 이후 코드로 변환
    @Enumerated(EnumType.STRING) var type: TransactionType,
    var amount: Long,
    var content: String,
    var date: LocalDateTime
) : BaseEntity()