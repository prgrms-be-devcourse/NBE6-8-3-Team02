package com.back.domain.goal.repository

import com.back.domain.goal.entity.Goal
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface GoalRepository : JpaRepository<Goal, Long> {
    fun id(id: Int): MutableList<Goal>
//    fun findByMemberId(memberId : Int, pageable: Pageable) : Page<Goal>
}