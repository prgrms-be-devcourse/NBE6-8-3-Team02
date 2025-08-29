package com.back.domain.goal.controller

import com.back.domain.goal.dto.GoalDto
import com.back.domain.goal.dto.GoalRequestDto
import com.back.domain.goal.service.GoalService
import com.back.global.rsData.RsData
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/goals")
@Tag(name = "GoalController", description = "목표 컨트롤러")
class ApiV1GoalController (
    private val goalService: GoalService
) {

//    @GetMapping
//    @Operation(summary = "다건 조회")
//    fun getGoals(
//        @AuthenticationPrincipal userDetails: CustomUserDetails,
//        @RequestParam(defaultValue = "0") page: Int,
//        @RequestParam(defaultValue = "10") size: Int
//    ): ResponseEntity<RsData<List<GoalDto>>> {
//        val goals = goalService.findByMember(userDetails.member, page, size)
//            .map { GoalDto.from(it) }
//
//        return ResponseEntity.ok(
//            RsData(
//                resultCode = "200-1",
//                msg = "목표(memberId: ${userDetails.member.id})를 조회합니다.",
//                data = goalDtos
//            )
//        )
//    }

    @GetMapping("/{id}")
    @Operation(summary = "단건 조회")
    fun getGoal(
        @PathVariable id: Long
    ): ResponseEntity<RsData<GoalDto>> {
        val goal = goalService.findById(id)

        return ResponseEntity.ok(
            RsData(
                resultCode = "200-1",
                msg = "목표(id: ${goal.id})를 조회합니다.",
                data = GoalDto.from(goal) // DTO의 팩토리 메소드 사용
            )
        )
    }

    @PostMapping
    @Operation(summary = "생성")
    fun create(
        //@AuthenticationPrincipal userDetails: CunstomUserDetails, // Member 팀 작업 전까지 주석 처리
        @Valid @RequestBody requestDto: GoalRequestDto
    ): ResponseEntity<RsData<GoalDto>> {
        val goal = goalService.create(/*userDetails.member,*/ requestDto) // Member 팀 작업 전까지 주석 처리

        return ResponseEntity.ok(
            RsData(
                resultCode = "201-1",
                msg = "목표(id: ${goal.id})가 생성되었습니다.",
                data = GoalDto.from(goal) // DTO의 팩토리 메소드 사용
            )
        )
    }

    @PutMapping("/{id}")
    @Operation(summary = "수정")
    fun modify(
        @PathVariable id: Int,
        @Valid @RequestBody requestDto: GoalRequestDto
    ): ResponseEntity<RsData<*>> {
        goalService.modify(id.toLong(), requestDto)

        return ResponseEntity.ok(
            RsData<Unit>(
                resultCode = "200-1",
                msg = "목표(id: $id)가 수정되었습니다.",
            )
        )
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "삭제")
    fun delete(
        @PathVariable id: Int,
    ): ResponseEntity<RsData<*>> {
        goalService.delete(id.toLong())

        return ResponseEntity.ok(
            RsData<Unit>(
                resultCode = "200-1",
                msg = "목표(id: $id)가 삭제되었습니다.",
            )
        )
    }
}
