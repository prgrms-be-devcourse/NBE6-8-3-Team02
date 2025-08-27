package com.back.domain.member.dto

import com.back.domain.member.entity.MemberRole
import java.time.LocalDateTime

data class MemberSignUpResponse(
    val id: Int,
    val email: String,
    val name: String,
    val phoneNumber: String,
    val role: MemberRole,
    val isActive: Boolean,
    val createDate: LocalDateTime,
    val modifyDate: LocalDateTime
)
