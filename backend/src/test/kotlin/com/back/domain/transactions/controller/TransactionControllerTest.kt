package com.back.domain.transactions.controller

import com.back.domain.transactions.dto.CreateTransactionRequestDto
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
class TransactionControllerTest (
    @Autowired private val mvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper
) {
    @Test
    @WithUserDetails("user@test.com")
    @DisplayName("""
        --통합 테스트--
        1. 거래 등록
        2. 단건 조회
        3. 전체 조회
        4. 삭제
    """)
    fun transactionFlowTest(){
        val createTransactionRequestDto = CreateTransactionRequestDto(
            1,
            "ADD",
            1000,
            "테스트",
            "2025-09-01T10:00:00"
        )

        val createResult = mvc.perform(post("/api/v1/transactions/asset")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createTransactionRequestDto)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.content").value("테스트"))
            .andExpect(jsonPath("$.data.amount").value(1000))
            .andReturn()

        val transactionId = objectMapper
            .readTree(createResult.response.contentAsString)
            .path("data").path("id").asInt()

        mvc.perform(get("/api/v1/transactions/asset/$transactionId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.id").value(transactionId))
            .andExpect(jsonPath("$.data.amount").value(1000))
            .andExpect(jsonPath("$.data.content").value("테스트"))

        mvc.perform(get("/api/v1/transactions/asset"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.length()").value(1))

        mvc.perform(delete("/api/v1/transactions/asset/$transactionId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.msg").value("${transactionId}번 거래를 삭제 했습니다."))

        mvc.perform(get("/api/v1/transactions/asset"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.length()").value(0))
    }
}