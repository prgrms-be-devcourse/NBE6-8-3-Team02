package com.back.domain.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class MemberLoginRequest(
    @field:Email(message = "올바른 이메일 형식이 아닙니다.")
    val email: String,
    @field:Size(min = 6, max = 20, message = "비밀번호는 6~20자 사이여야 합니다.")
    val password: String

)
