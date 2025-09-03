package com.back.domain.notices.controller

import com.back.domain.notices.dto.CreateNoticeRequestDto
import com.back.domain.notices.dto.UpdateNoticeRequestDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class NoticeControllerTest (
    @Autowired private val mvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper
) {
    @Test
    @WithUserDetails("admintest@test.com")
    @DisplayName("""
        --통합 테스트--
        1. 공지사항 등록
        2. 단건 조회
        3. 수정
        4. 전체 조회
        5. 삭제
    """)
    fun noticeFlowTest(){
        val noticeCreateRequestDto = CreateNoticeRequestDto(
            "제목",
            "내용",
            "URL"
        )

        val createResult = mvc.perform(post("/api/v1/notices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(noticeCreateRequestDto)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.title").value("제목"))
            .andExpect(jsonPath("$.data.content").value("내용"))
            .andExpect(jsonPath("$.data.writerName").value("관리자"))
            .andReturn()

        val noticeId = objectMapper
            .readTree(createResult.response.contentAsString)
            .path("data").path("id").asInt()

        mvc.perform(get("/api/v1/notices/$noticeId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.id").value(noticeId))
            .andExpect(jsonPath("$.data.title").value("제목"))
            .andExpect(jsonPath("$.data.content").value("내용"))
            .andReturn()

        val updateRequest = UpdateNoticeRequestDto(
            "수정 제목",
            "수정 내용"
        )

        mvc.perform(put("/api/v1/notices/$noticeId")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.id").value(noticeId))
            .andExpect(jsonPath("$.data.title").value("수정 제목"))
            .andExpect(jsonPath("$.data.content").value("수정 내용"))
            .andReturn()

        mvc.perform(get("/api/v1/notices"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.content.length()").value(1))

        mvc.perform(delete("/api/v1/notices/$noticeId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.msg").value("공지사항이 삭제되었습니다."))

        mvc.perform(get("/api/v1/notices"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.content.length()").value(0))
    }
}