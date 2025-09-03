package com.back.domain.goal.controller

import com.back.domain.goal.dto.GoalDto
import com.back.domain.goal.dto.GoalRequestDto
import com.back.domain.goal.service.GoalService
import com.back.global.rsData.RsData
import com.back.global.security.CustomMemberDetails
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/goals")
@Tag(name = "Goal", description = "목표 관련 API")
class ApiV1GoalController(
    private val goalService: GoalService
) {

    @GetMapping
    @Operation(summary = "다건 조회")
    fun getGoals(
        @AuthenticationPrincipal userDetails: CustomMemberDetails,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<RsData<List<GoalDto>>> {
        val goals = goalService.findByMember(userDetails.getMember(), page, size)
        val goalDtos = goals.map { GoalDto.from(it) }

        return ResponseEntity.ok(
            RsData(
                resultCode = "200-1",
                msg = "목표(memberId: ${userDetails.getMember()})를 조회합니다.",
                data = goalDtos
            )
        )
    }

    @GetMapping("/{id}")
    @Operation(summary = "단건 조회")
    fun getGoal(@PathVariable id: Int): ResponseEntity<RsData<GoalDto>> {
        val goal = goalService.findById(id)

        return ResponseEntity.ok(
            RsData(
                resultCode = "200-1",
                msg = "목표(id: ${goal.id})를 조회합니다.",
                data = GoalDto.from(goal)
            )
        )
    }

    @PostMapping
    @Operation(summary = "생성")
    fun create(
        @AuthenticationPrincipal userDetails: CustomMemberDetails,
        @Valid @RequestBody requestDto: GoalRequestDto
    ): ResponseEntity<RsData<GoalDto>> {
        val goal = goalService.create(userDetails.getMember(), requestDto)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                RsData(
                    resultCode = "201-1",
                    msg = "목표(id: ${goal.id})가 작성되었습니다.",
                    data = GoalDto.from(goal)
                )
            )
    }

    @PutMapping("/{id}")
    @Operation(summary = "수정")
    fun modify(
        @PathVariable id: Int,
        @Valid @RequestBody requestDto: GoalRequestDto
    ): ResponseEntity<RsData<*>> {
        goalService.modify(id, requestDto)

        return ResponseEntity.ok(
            RsData<Unit>(
                resultCode = "200-1",
                msg = "목표(id: $id)가 수정되었습니다."
            )
        )
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "삭제")
    fun delete(@PathVariable id: Int): ResponseEntity<RsData<*>> {
        goalService.delete(id)

        return ResponseEntity.ok(
            RsData<Unit>(
                resultCode = "200-1",
                msg = "목표(id: $id)가 삭제되었습니다."
            )
        )
    }
}

