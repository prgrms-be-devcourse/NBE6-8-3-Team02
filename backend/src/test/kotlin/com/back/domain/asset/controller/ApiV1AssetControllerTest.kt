package com.back.domain.asset.controller

import com.back.domain.asset.service.AssetService
import com.back.domain.member.service.MemberService
import com.back.global.initData.BaseInitData
import org.junit.jupiter.api.BeforeEach
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertEquals

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class ApiV1AssetControllerTest{
    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var assetService: AssetService

    @Autowired
    private lateinit var memberService: MemberService

    @Autowired
    private lateinit var baseInitData: BaseInitData

    @BeforeEach
    fun setUp() {
        baseInitData.initializeAllData()
    }

    @Test
    @DisplayName("자산 등록")
    @WithUserDetails("user1@test.com")
    fun create() {
        val member = memberService.findMemberByEmail("user1@test.com")

        val resultActions = mvc
            .perform(
                post("/api/v1/assets")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                            {
                                "memberId": "${member.id}",
                                "name": "테스트적금",
                                "assetType": "DEPOSIT",
                                "assetValue": "100000"
                            }
                            """
                    )
            )
            .andDo(print())

        val asset = assetService.findAllByMemberId(2).last()
        resultActions
            .andExpect(handler().handlerType(ApiV1AssetController::class.java))
            .andExpect(handler().methodName("createAsset"))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.resultCode").value("201-1"))
            .andExpect(jsonPath("$.msg").value("자산이 등록되었습니다."))
            .andExpect(jsonPath("$.data.id").value(asset.id))
            .andExpect(jsonPath("$.data.name").value(asset.name))
            .andExpect(jsonPath("$.data.assetType").value(asset.assetType.toString()))
            .andExpect(jsonPath("$.data.assetValue").value(asset.assetValue))
    }

    @Test
    @DisplayName("자산 다건 조회")
    @WithUserDetails("user1@test.com")
    fun gets() {
        val resultActions = mvc
            .perform(
                get("/api/v1/assets")
            )
            .andDo(print())

        resultActions
            .andExpect(handler().handlerType(ApiV1AssetController::class.java))
            .andExpect(handler().methodName("getAssets"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.resultCode").value("200-1"))
            .andExpect(jsonPath("$.msg").value("자산 목록을 조회했습니다."))
            .andExpect(jsonPath("$.data.length()").value(assetService.count()))
    }

    @Test
    @DisplayName("자산 단건 조회")
    @WithUserDetails("user1@test.com")
    fun get() {
        val id = 1

        val resultActions = mvc
            .perform(
                get("/api/v1/assets/${id}")
            )
            .andDo(print())

        val asset = assetService.findById(id)
        resultActions
            .andExpect(handler().handlerType(ApiV1AssetController::class.java))
            .andExpect(handler().methodName("getAsset"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.resultCode").value("200-1"))
            .andExpect(jsonPath("$.msg").value("${id}번 자산을 조회했습니다."))
            .andExpect(jsonPath("$.data.id").value(asset.id))
            .andExpect(jsonPath("$.data.name").value(asset.name))
            .andExpect(jsonPath("$.data.assetType").value(asset.assetType.toString()))
            .andExpect(jsonPath("$.data.assetValue").value(asset.assetValue))
    }

    @Test
    @DisplayName("자산 삭제")
    @WithUserDetails("user1@test.com")
    fun delete() {
        val id = 1
        val num = assetService.count()

        val resultActions = mvc
            .perform(
                delete("/api/v1/assets/${id}")
            )
            .andDo(print())

        resultActions
            .andExpect(handler().handlerType(ApiV1AssetController::class.java))
            .andExpect(handler().methodName("deleteAsset"))
            .andExpect(status().isNoContent)
            .andExpect(jsonPath("$.resultCode").value("204-1"))
            .andExpect(jsonPath("$.msg").value("${id}번 자산을 삭제했습니다."))

        assertEquals(num-1 , assetService.count())
    }

    @Test
    @DisplayName("자산 수정")
    @WithUserDetails("user1@test.com")
    fun update() {
        val id = 1

        val resultActions = mvc
            .perform(
                put("/api/v1/assets/${id}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                            {
                                "id": "$id",
                                "name": "적금수정",
                                "assetType": "REAL_ESTATE",
                                "assetValue": "123456"
                            }
                            """
                    )
            )
            .andDo(print())

        val asset = assetService.findById(id)
        resultActions
            .andExpect(handler().handlerType(ApiV1AssetController::class.java))
            .andExpect(handler().methodName("updateAsset"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.resultCode").value("200-1"))
            .andExpect(jsonPath("$.msg").value("${id}번 자산을 수정했습니다."))
            .andExpect(jsonPath("$.data.id").value(asset.id))
            .andExpect(jsonPath("$.data.name").value(asset.name))
            .andExpect(jsonPath("$.data.assetType").value(asset.assetType.toString()))
            .andExpect(jsonPath("$.data.assetValue").value(asset.assetValue))
    }
}