package com.back.domain.goal.repository

import com.back.domain.goal.entity.Goal
import com.back.domain.member.entity.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface GoalRepository : JpaRepository<Goal, Int> {
    fun findByMember(member : Member, pageable: Pageable) : Page<Goal>

}