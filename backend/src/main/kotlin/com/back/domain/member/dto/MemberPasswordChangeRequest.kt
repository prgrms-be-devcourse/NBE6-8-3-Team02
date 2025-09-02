package com.back.domain.member.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class MemberPasswordChangeRequest(
    @field:NotBlank(message = "현재 비밀번호는 필수입니다.")
    val currentPassword: String,
    @field:NotBlank(message = "새 비밀번호는 필수입니다.")
    @field:Size(min = 6, max = 20, message = "비밀번호는 6~20자 사이여야 합니다.")
    val newPassword: String
)