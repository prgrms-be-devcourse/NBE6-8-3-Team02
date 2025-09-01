package com.back.domain.member.dto

import com.back.domain.member.entity.MemberRole
import java.time.LocalDateTime

data class MemberResponse(
    val memberId:Int,
    val email:String,
    val name:String,
    val phoneNumber:String,
    val role: String,
    val isActive:Boolean,
    val createDate: LocalDateTime,
    val modifyDate: LocalDateTime
)
