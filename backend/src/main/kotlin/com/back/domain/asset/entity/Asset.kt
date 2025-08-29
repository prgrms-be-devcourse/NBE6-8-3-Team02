package com.back.domain.asset.entity

import com.back.domain.member.entity.Member
import com.back.global.jpa.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class Asset(
    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "member_id", nullable = false)
    var member: Member,

    var name: String,

    @field:Enumerated(EnumType.STRING)
    var assetType: AssetType,

    var assetValue: Long,

    @field:Column(nullable = false)
    var status: Boolean
) : BaseEntity() {

}