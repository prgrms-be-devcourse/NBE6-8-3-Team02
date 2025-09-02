package com.back.domain.auth.dto

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class FindAccountRequest (
    @field:Size(min = 2, max = 20, message = "이름은 2~20자 사이여야 합니다.")
    val name:String,
    @field:Pattern(regexp = "^\\d{11}$", message = "올바른 전화번호 형식이 아닙니다. (예: 01012345678)")
    val phoneNumber: String
)