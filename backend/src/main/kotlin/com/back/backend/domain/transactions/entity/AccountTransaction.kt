package com.back.backend.domain.transactions.entity

import com.back.backend.global.jpa.entity.BaseEntity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDateTime

class AccountTransaction : BaseEntity() {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var account : Account? = null;
    // Account 도메인이 추가되어야함.

    @Enumerated(EnumType.STRING)
    var type: TransactionType = TransactionType.REMOVE

    var amount: Long? = 0L

    var content: String? = null // 필요 메모

    var date : LocalDateTime? = null // 체결일

    // int id(PK) -> BaseEntity
    // LocalDateTime created_at -> BaseEntity
    // LocalDateTime modified_at -> BaseEntity
}