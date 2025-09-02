package com.back.domain.notices.dto

import com.back.domain.notices.entity.Notice
import java.time.LocalDateTime

data class NoticeResponseDto (
    val id: Int,
    val title: String,
    val content: String,
    val views: Int,
    val fileUrl: String?,
    val writerName: String,
    val createDate: LocalDateTime,
    val updateDate: LocalDateTime
) {
    constructor(notice: Notice) : this(
        notice.id,
        notice.title,
        notice.content,
        notice.views,
        notice.fileUrl,
        notice.member.name,
        notice.createDate,
        notice.modifyDate
    )

    companion object {
        fun from(notice: Notice): NoticeResponseDto {
            return NoticeResponseDto(
                notice.id,
                notice.title,
                notice.content,
                notice.views,
                notice.fileUrl,
                notice.member.name,
                notice.createDate,
                notice.modifyDate
            )
        }
    }
}