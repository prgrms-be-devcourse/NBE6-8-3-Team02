package com.back.domain.member.repository

import com.back.domain.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member, Long> {
    fun existsByEmailAndIsDeletedFalse(email: String): Boolean
    fun findByEmail(email: String): Member?
}