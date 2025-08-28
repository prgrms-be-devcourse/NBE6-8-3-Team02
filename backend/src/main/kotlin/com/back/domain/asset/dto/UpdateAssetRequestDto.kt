package com.back.domain.asset.dto

data class UpdateAssetRequestDto(
    val id: Int,
    val name: String,
    val assetType: String,
    val assetValue: Long
) {

}