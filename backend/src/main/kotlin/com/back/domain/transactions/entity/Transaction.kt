package com.back.domain.transactions.entity

import com.back.domain.asset.entity.Asset
import com.back.global.jpa.entity.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDateTime

@Entity
class Transaction (
    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "asset_id", nullable = false)
    @field:OnDelete(action = OnDeleteAction.CASCADE)
    val asset : Asset,
    @field:Enumerated(EnumType.STRING) var type : TransactionType,
    var amount: Long,
    var content: String,
    var date: LocalDateTime
) : BaseEntity()