package com.back.backend.domain.transactions.entity

import com.back.backend.global.jpa.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.time.LocalDateTime

@Entity
class Transaction : BaseEntity() {
    /*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var asset : Asset? = null;
     */// Asset 도메인이 추가되어야함.

    @Enumerated(EnumType.STRING)
    var type: TransactionType = TransactionType.REMOVE

    var amount : Long? = 0L // 거래 량

    var content: String? = null // 필요 메모

    var date : LocalDateTime? = null // 체결일

    // int id(PK) -> BaseEntity
    // LocalDateTime created_at -> BaseEntity
    // LocalDateTime modified_at -> BaseEntity
}