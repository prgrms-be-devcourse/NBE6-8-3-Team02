package com.back.domain.goal.dto

import com.back.domain.goal.entity.GoalStatus
import jakarta.validation.constraints.Size
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class GoalRequestDto (
    @field:NotBlank
    @field:Size
    val description: String,

    @field:NotNull
    val currentAmount: Long,

    @field:NotNull
    val targetAmount: Long,

    @field:NotNull
    val deadline: LocalDateTime?,
    val status: GoalStatus,

    )