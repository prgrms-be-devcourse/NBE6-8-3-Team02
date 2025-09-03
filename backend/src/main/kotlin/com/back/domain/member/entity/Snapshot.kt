package com.back.domain.member.entity

import com.back.global.jpa.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "asset_snapshot")
class Snapshot(
    @ManyToOne
    @JoinColumn(name = "member_id")
    val member: Member,
    @Column(name = "asset_year")
    val year: Int,
    @Column(name = "asset_month")
    val month: Int,
    var totalAsset: Long
) : BaseEntity() {

    fun updateTotalAsset(totalAsset: Long) {
        this.totalAsset = totalAsset
    }
}