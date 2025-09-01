package com.back.domain.auth.dto

data class FindAccountRequest (
    val name:String,
    val phoneNumber: String
)