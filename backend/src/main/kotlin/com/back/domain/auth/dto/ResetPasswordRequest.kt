package com.back.domain.auth.dto

data class ResetPasswordRequest(
    val email:String,
    val name:String,
    val phoneNumber:String
)
