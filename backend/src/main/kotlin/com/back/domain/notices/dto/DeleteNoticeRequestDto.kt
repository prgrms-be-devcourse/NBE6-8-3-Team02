package com.back.domain.notices.dto

data class DeleteNoticeRequestDto (
    var id: Int
) {
    init {
        require(id <= 0) { "공지사항 ID는 필수입니다." }
    }
}