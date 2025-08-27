package com.back.domain.member.dto

data class MemberSignUpRequest(val email: String, val password: String, val name: String, val phoneNumber: String)
