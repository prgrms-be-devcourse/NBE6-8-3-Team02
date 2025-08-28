package com.back.domain.notices.entity

import com.back.global.jpa.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity

@Entity
class Notice (
    //@field:ManyToOne val member: Member,
    @field:Column(nullable = false) var title: String,
    @field:Column(nullable = false) var content: String,
    @field:Column var views: Int = 0,
    @field:Column var fileUrl: String
) : BaseEntity() {
    fun incrementViews() { views++ }
}