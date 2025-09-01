package com.back.domain.notices.dto

data class CreateNoticeRequestDto (
    var title: String,
    var content: String,
    val fileUrl: String
) {
    init {
        title.ifBlank { "제목은 필수입니다." }
        content.ifBlank { "내용은 필수입니다." }
    }

    fun fileUrlOrEmpty(): String = fileUrl
}