package com.back.domain.notices.repository

import com.back.domain.notices.entity.Notice
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface NoticeRepositoryCustom {
    fun findByTitleContainingIgnoreCase(title: String, pageable: Pageable): Page<Notice>
}