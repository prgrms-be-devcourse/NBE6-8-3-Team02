package com.back.domain.account.controller

import com.back.domain.account.dto.AccountDtoKt
import com.back.domain.account.dto.RqCreateAccountDto
import com.back.domain.account.dto.RqUpdateAccountDto
import com.back.domain.account.entity.Account
import com.back.domain.account.service.AccountService
import com.back.domain.member.entity.Member
import com.back.global.security.CustomMemberDetails
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@DisplayName("Account 컨트롤러 Kotlin 테스트")
class ApiV1AccountControllerKotlinTest {

    private lateinit var mockMvc: MockMvc
    private lateinit var accountService: AccountService
    private lateinit var objectMapper: ObjectMapper

    private lateinit var member: Member
    private lateinit var account: Account
    private lateinit var createDto: RqCreateAccountDto
    private lateinit var updateDto: RqUpdateAccountDto
    private lateinit var userDetails: CustomMemberDetails

    @BeforeEach
    fun setUp() {
        // Mock 객체 생성
        accountService = mock(AccountService::class.java)
        objectMapper = ObjectMapper()
        
        // MockMvc 생성 (StandaloneSetup 사용)
        mockMvc = MockMvcBuilders.standaloneSetup(ApiV1AccountController(accountService))
            .build()
        
        member = Member(
            email = "test@test.com",
            password = "password123",
            name = "테스트유저",
            phoneNumber = "010-1234-5678"
        )
        
        account = Account(
            member = member,
            name = "테스트계좌",
            accountNumber = "123-456-789",
            balance = 10000L
        )
        
        createDto = RqCreateAccountDto(
            name = "테스트계좌",
            accountNumber = "123-456-789",
            balance = 10000L
        )
        
        updateDto = RqUpdateAccountDto(
            accountNumber = "987-654-321"
        )
        
        userDetails = mock(CustomMemberDetails::class.java)
        `when`(userDetails.getMember()).thenReturn(member)
    }

    @Test
    @DisplayName("계좌 생성 테스트")
    @WithMockUser
    fun `계좌 생성 테스트`() {
        // given
        `when`(accountService.createAccount(any(), any())).thenReturn(account)
        
        val requestBody = objectMapper.writeValueAsString(createDto)

        // when & then
        mockMvc.perform(
            post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.name").value(account.name))
            .andExpect(jsonPath("$.accountNumber").value(account.accountNumber))
            .andExpect(jsonPath("$.balance").value(account.balance))
    }

    @Test
    @DisplayName("계좌 다건 조회 테스트")
    @WithMockUser
    fun `계좌 다건 조회 테스트`() {
        // given
        val accounts = listOf(account)
        `when`(accountService.getAccountsByMemberId(any())).thenReturn(accounts)

        // when & then
        mockMvc.perform(
            get("/api/v1/accounts")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].name").value(account.name))
            .andExpect(jsonPath("$[0].accountNumber").value(account.accountNumber))
            .andExpect(jsonPath("$[0].balance").value(account.balance))
    }

    @Test
    @DisplayName("계좌 단건 조회 테스트")
    @WithMockUser
    fun `계좌 단건 조회 테스트`() {
        // given
        val accountId = 1
        `when`(accountService.getAccount(accountId, member)).thenReturn(account)

        // when & then
        mockMvc.perform(
            get("/api/v1/accounts/$accountId")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value(account.name))
            .andExpect(jsonPath("$.accountNumber").value(account.accountNumber))
            .andExpect(jsonPath("$.balance").value(account.balance))
    }

    @Test
    @DisplayName("계좌 수정 테스트")
    @WithMockUser
    fun `계좌 수정 테스트`() {
        // given
        val accountId = 1
        doNothing().`when`(accountService).updateAccount(accountId, member, updateDto)
        
        val requestBody = objectMapper.writeValueAsString(updateDto)

        // when & then
        mockMvc.perform(
            put("/api/v1/accounts/$accountId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isNoContent)
    }

    @Test
    @DisplayName("계좌 삭제 테스트")
    @WithMockUser
    fun `계좌 삭제 테스트`() {
        // given
        val accountId = 1
        doNothing().`when`(accountService).deleteAccount(accountId, member)

        // when & then
        mockMvc.perform(
            delete("/api/v1/accounts/$accountId")
        )
            .andExpect(status().isNoContent)
    }

    @Test
    @DisplayName("계좌 생성 실패 테스트 - 잘못된 요청 데이터")
    @WithMockUser
    fun `계좌 생성 실패 테스트 - 잘못된 요청 데이터`() {
        // given
        val invalidDto = RqCreateAccountDto(
            name = "",
            accountNumber = "",
            balance = -1000L
        )
        
        val requestBody = objectMapper.writeValueAsString(invalidDto)

        // when & then
        mockMvc.perform(
            post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("계좌 수정 실패 테스트 - 잘못된 요청 데이터")
    @WithMockUser
    fun `계좌 수정 실패 테스트 - 잘못된 요청 데이터`() {
        // given
        val accountId = 1
        val invalidDto = RqUpdateAccountDto(
            accountNumber = ""
        )
        
        val requestBody = objectMapper.writeValueAsString(invalidDto)

        // when & then
        mockMvc.perform(
            put("/api/v1/accounts/$accountId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isBadRequest)
    }
}
