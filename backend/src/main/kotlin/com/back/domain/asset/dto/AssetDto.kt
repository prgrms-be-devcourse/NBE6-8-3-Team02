package com.back.domain.asset.dto

import com.back.domain.asset.entity.Asset
import java.time.LocalDateTime

data class AssetDto(
    val id: Int,
    //val memberId: Int,
    val name: String,
    val assetType: String,
    val assetValue: Long,
    val createDate: LocalDateTime,
    val modifyDate: LocalDateTime
) {
    constructor(asset: Asset) : this(
        asset.id,
        //asset.getMember().getId(),
        asset.name,
        asset.assetType.toString(),
        asset.assetValue,
        asset.createDate,
        asset.modifyDate
    )
}