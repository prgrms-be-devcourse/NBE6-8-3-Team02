package com.back.domain.member.dto

import com.back.domain.member.entity.Member

data class SnapshotResponse(
    val year:Int,
    val month:Int,
    val totalAsset: Long
) {
}