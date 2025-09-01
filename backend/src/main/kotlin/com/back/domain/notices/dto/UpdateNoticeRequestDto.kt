package com.back.domain.notices.dto

import com.back.domain.notices.entity.Notice

data class UpdateNoticeRequestDto (
    var title: String,
    var content: String,
    val fileUrl: String?
) {
    constructor(notice: Notice) : this(
        title = notice.title,
        content = notice.content,
        fileUrl = notice.fileUrl
    )
}