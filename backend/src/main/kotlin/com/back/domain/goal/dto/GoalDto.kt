package com.back.domain.goal.dto

import com.back.domain.goal.entity.Goal
import com.back.domain.goal.entity.GoalStatus

import com.sun.beans.introspect.PropertyInfo
import java.time.LocalDateTime

data class GoalDto (
    val id: Int?,
    val memberId: Int,
    val description: String,
    val currentAmount: Long,
    val targetAmount: Long,
    val deadline: LocalDateTime?,
    val status: GoalStatus,

) {
    companion object {
        fun from(goal: Goal): GoalDto {
            return GoalDto(
                id = goal.id,
                memberId = goal.member.id,
                description = goal.description,
                currentAmount = goal.currentAmount,
                targetAmount = goal.targetAmount,
                deadline = goal.deadling,
                status = goal.status,
            )
        }
    }
}
