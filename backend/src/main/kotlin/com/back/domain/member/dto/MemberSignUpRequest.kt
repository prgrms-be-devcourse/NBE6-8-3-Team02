package com.back.domain.member.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class MemberSignUpRequest(
    @field:Email(message = "올바른 이메일 형식이 아닙니다.")
    @field:NotBlank(message = "이메일은 필수입니다.")
    val email: String,
    @field:NotBlank(message = "비밀번호는 필수입니다.")
    @field:Size(min = 6, max = 20, message = "비밀번호는 6~20자 사이여야 합니다.")
    val password: String,
    @field:NotBlank(message = "이름은 필수입니다.")
    @field:Size(min = 2, max = 20, message = "이름은 2~20자 사이여야 합니다.")
    val name: String,
    @field:Pattern(
        regexp = "^\\d{11}$",
        message = "올바른 전화번호 형식이 아닙니다. (예: 01012345678)"
    )
    val phoneNumber: String
)
