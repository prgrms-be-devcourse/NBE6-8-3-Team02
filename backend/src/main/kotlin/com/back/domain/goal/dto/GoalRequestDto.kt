package com.back.domain.goal.dto

import com.back.domain.goal.entity.Goal
import com.back.domain.member.entity.Member
import jakarta.validation.constraints.Size
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class GoalRequestDto(
    @field:NotBlank
    @field:Size(min = 2, max = 100)
    val description: String,

    @field:NotNull
    val targetAmount: Long,

    @field:NotNull
    val deadline: LocalDateTime?, // Nullable로 변경
) {
    // Member 정보를 파라미터로 받도록 수정
    fun toEntity(member: Member): Goal {
        return Goal(
            member = member, // 전달받은 member를 사용
            description = this.description,
            targetAmount = this.targetAmount,
            deadline = this.deadline
        )
    }
}