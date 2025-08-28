package com.back.domain.asset.dto

data class CreateWithoutMemberDto(
    val name: String,
    val assetType: String,
    val assetValue: Long
) {

}