package com.back.domain.notices.entity

import com.back.domain.notices.dto.UpdateNoticeRequestDto
import com.back.global.jpa.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity

@Entity
class Notice (
    //@field:ManyToOne val member: Member,
    @Column(nullable = false) var title: String,
    @Column(nullable = false) var content: String,
    @Column var views: Int = 0,
    @Column var fileUrl: String
    //굳이 var로 선언되어야 할까? 한번 고민.
) : BaseEntity() {
    fun incrementViews() { views++ }
    fun update(dto: UpdateNoticeRequestDto) {
        dto.title.ifBlank { null }?.let { title = it }
        dto.content.ifBlank { null }?.let { content = it }
        dto.fileUrl.ifBlank { null }?.let { fileUrl = it }
    }
}