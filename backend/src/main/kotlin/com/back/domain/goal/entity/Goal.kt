package com.back.domain.goal.entity

import com.back.domain.member.entity.Member
import com.back.global.jpa.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.ManyToOne
import org.hibernate.annotations.SoftDelete
import java.time.LocalDateTime


@Entity
@SoftDelete
class Goal(
    @ManyToOne
    var member: Member,

    var description: String,
    var targetAmount: Long,
    var currentAmount: Long = 0L,
    var deadline: LocalDateTime?,

    @Enumerated(EnumType.STRING)
    var status: GoalStatus = GoalStatus.NOT_STARTED

) : BaseEntity() {

    val memberId: Int
        get() = member.id

    fun update(
        description: String,
        targetAmount: Long,
        deadline: LocalDateTime?,
    ) {
        this.description = description
        this.targetAmount = targetAmount
        this.deadline = deadline
    }

    fun updateCurrentAmount(currentAmount: Long) {
        this.currentAmount = currentAmount
    }

    fun updateStatus(status: GoalStatus) {
        this.status = status
    }
}