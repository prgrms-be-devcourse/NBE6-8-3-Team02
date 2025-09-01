package com.back.domain.notices.entity

import com.back.domain.member.entity.Member
import com.back.domain.notices.dto.UpdateNoticeRequestDto
import com.back.global.jpa.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne

@Entity
class Notice (
    @ManyToOne val member: Member,
    @Column(nullable = false) var title: String,
    @Column(nullable = false) var content: String,
    @Column var views: Int = 0,
    @Column var fileUrl: String? = null
) : BaseEntity() {
    fun incrementViews() { views++ }
    fun update(dto: UpdateNoticeRequestDto) {
        dto.title.ifBlank { null }?.let { title = it }
        dto.content.ifBlank { null }?.let { content = it }
        dto.fileUrl?.ifBlank { null }?.let { fileUrl = it }
    }
}