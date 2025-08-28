package com.back.domain.notices.dto

import com.back.domain.notices.entity.Notice
import java.time.LocalDateTime

data class NoticeResponseDto (
    var id: Int,
    var title: String,
    var content: String,
    var views: Int,
    var filrUrl: String,
    var wrtierName: String,
    val createDate: LocalDateTime,
    val updateDate: LocalDateTime
) {
    constructor(notice: Notice) : this(
        id = notice.id,
        title = notice.title,
        content = notice.content,
        views = notice.views,
        filrUrl = notice.fileUrl,
        wrtierName = "", //notice.member.username
        createDate = notice.createDate,
        updateDate = notice.modifyDate
    )

    companion object {
        fun from(notice: Notice): NoticeResponseDto {
            return NoticeResponseDto(
                notice.id,
                notice.title,
                notice.content,
                notice.views,
                notice.fileUrl,
                //notice.member,
                "", // 임시, Member 구현 이후 위의 것으로 대체.
                notice.createDate,
                notice.modifyDate
            )
        }
    }
}