package com.back.domain.asset.dto

data class CreateAssetRequestDto(
    val memberId: Int,
    val name: String,
    val assetType: String,
    val assetValue: Long
) {

}