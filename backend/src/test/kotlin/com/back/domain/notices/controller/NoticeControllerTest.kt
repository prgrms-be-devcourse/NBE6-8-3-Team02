package com.back.domain.notices.controller

import com.back.domain.notices.dto.CreateNoticeRequestDto
import com.back.domain.notices.dto.NoticeResponseDto
import com.back.domain.notices.service.NoticeService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import java.time.LocalDateTime

@WebMvcTest(ApiV1NoticeController::class)
class NoticeControllerTest (
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {
    @MockitoBean
    private lateinit var noticeService: NoticeService

    @Test
    @DisplayName("공지사항 생성 API 테스트")
    @WithMockUser(username = "admin@test.com",  roles = ["ADMIN"])
    fun createNoticeTest() {
        val dto = CreateNoticeRequestDto("테스트 제목", "테스트 내용", "테스트 url")
        val mockNotice = NoticeResponseDto(
            1,
            dto.title,
            dto.content,
            0,
            "",
            "admin@test.com",
            LocalDateTime.parse("2025-09-01T10:00:00"),
            LocalDateTime.parse("2025-09-01T12:00:00")
        )
    }
}