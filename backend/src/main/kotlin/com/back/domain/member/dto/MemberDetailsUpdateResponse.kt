package com.back.domain.member.dto

import java.time.LocalDateTime

data class MemberDetailsUpdateResponse(
    val memberId: Int,
    val email: String,
    val name: String,
    val phoneNumber: String,
    val role:String,
    val isActive:Boolean,
    val createDate: LocalDateTime,
    val modifyDate: LocalDateTime
)