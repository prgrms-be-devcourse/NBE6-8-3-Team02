package com.back.domain.goal.controller

import com.back.domain.goal.service.GoalService
import com.back.domain.member.repository.MemberRepository
import org.hamcrest.Matchers.*
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import java.time.temporal.ChronoUnit

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class ApiV1GoalControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var goalService: GoalService



    @Test
    @DisplayName("목표 다건 조회")
    @WithUserDetails("usertest@test.com")
    fun getGoals() {
        mvc.perform(
            get("/api/v1/goals")
        )
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(handler().handlerType(ApiV1GoalController::class.java))
            .andExpect(handler().methodName("getGoals"))
            .andExpect(jsonPath("$.resultCode").value("200-1"))
            .andExpect(jsonPath("$.data.length()").value(1))
    }

    @Test
    @DisplayName("목표 단건 조회")
    @WithUserDetails("usertest@test.com")
    fun getGoal() {
        val id = 1
        val goal = goalService.findById(id)
        val expectedDeadline = goal.deadline?.truncatedTo(ChronoUnit.SECONDS).toString()

        mvc.perform(get("/api/v1/goals/$id"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(handler().handlerType(ApiV1GoalController::class.java))
            .andExpect(handler().methodName("getGoal"))
            .andExpect(jsonPath("$.resultCode").value("200-1"))
            .andExpect(jsonPath("$.msg").value("목표(id: $id)를 조회합니다."))
            .andExpect(jsonPath("$.data.id").value(goal.id))
            .andExpect(jsonPath("$.data.memberId").value(goal.memberId))
            .andExpect(jsonPath("$.data.description").value(goal.description))
            .andExpect(jsonPath("$.data.currentAmount").value(goal.currentAmount))
            .andExpect(jsonPath("$.data.targetAmount").value(goal.targetAmount))
            // .value(expectedDeadline) 대신 startsWith를 사용합니다.
            .andExpect(jsonPath("$.data.deadline").value(startsWith(expectedDeadline)))
            .andExpect(jsonPath("$.data.status").value(goal.status.name))
    }

    @Test
    @DisplayName("목표 생성")
    @WithUserDetails("usertest@test.com")
    fun create() {
        mvc.perform(
            post("/api/v1/goals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "description": "새로운 테스트 목표",
                        "targetAmount": "500000",
                        "deadline": "2026-01-01T00:00:00"
                    }
                    """.trimIndent()
                )
        )
            .andDo(print())
            .andExpect(status().isCreated)
            .andExpect(handler().handlerType(ApiV1GoalController::class.java))
            .andExpect(handler().methodName("create"))
            .andExpect(jsonPath("$.resultCode").value("201-1"))
    }

    @Test
    @DisplayName("목표 수정")
    @WithUserDetails("usertest@test.com")
    fun modify() {
        val id = 1

        mvc.perform(
            put("/api/v1/goals/$id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "description": "수정된 테스트 목표",
                        "targetAmount": "2000000",
                        "deadline": "2027-02-01T00:00:00"
                    }
                    """.trimIndent()
                )
        )
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(handler().handlerType(ApiV1GoalController::class.java))
            .andExpect(handler().methodName("modify"))
            .andExpect(jsonPath("$.resultCode").value("200-1"))
            .andExpect(jsonPath("$.msg").value("목표(id: $id)가 수정되었습니다."))
    }

    @Test
    @DisplayName("목표 삭제")
    @WithUserDetails("usertest@test.com")
    fun delete() {
        val id = 1

        mvc.perform(delete("/api/v1/goals/$id"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(handler().handlerType(ApiV1GoalController::class.java))
            .andExpect(handler().methodName("delete"))
            .andExpect(jsonPath("$.resultCode").value("200-1"))
            .andExpect(jsonPath("$.msg").value("목표(id: $id)가 삭제되었습니다."))
    }
}

