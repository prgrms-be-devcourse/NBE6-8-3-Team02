package com.back.domain.auth.dto

import com.back.domain.member.entity.MemberRole

data class MemberLoginResponse(
    val memberId: Int,
    val email: String,
    val name:String,
    val role: MemberRole
)
