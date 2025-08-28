package com.back.domain.notices.repository

import com.back.domain.notices.entity.Notice
import com.back.domain.notices.entity.QNotice
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class NoticeRepositoryImpl (
    private val queryFactory: JPAQueryFactory
) : NoticeRepositoryCustom {
    private val notice = QNotice.notice

    override fun findByTitleContainingIgnoreCase(title: String, pageable: Pageable): Page<Notice> {
        val query = queryFactory
            .selectFrom(notice)
            .where(
                notice.title.lower().like("%${title.lowercase()}%")
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        val results = query.fetch()
        val total = queryFactory
            .select(notice.count())
            .from(notice)
            .where(
                notice.title.lower().like("%${title.lowercase()}%")
            )
            .fetchOne() ?: 0L

        return PageImpl(results, pageable, total)
    }
}