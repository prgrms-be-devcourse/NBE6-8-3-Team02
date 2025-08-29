package com.back.domain.member.dto

data class MemberDetailsUpdateRequest(
    val name: String? = null,
    val phoneNumber: String? = null,
)
