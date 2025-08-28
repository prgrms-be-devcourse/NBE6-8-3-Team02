package com.back.domain.notices.repository

import com.back.domain.notices.entity.Notice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NoticeRepository : JpaRepository<Notice, Int>, NoticeRepositoryCustom {
}