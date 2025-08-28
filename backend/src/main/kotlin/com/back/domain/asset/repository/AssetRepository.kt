package com.back.domain.asset.repository

import com.back.domain.asset.entity.Asset
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface AssetRepository : JpaRepository<Asset, Int> {
    fun findAllByStatusTrue(): List<Asset>
    fun findAllByStatusTrueAndMemberId(memberId: Int): List<Asset>
    fun countAllByStatusTrue(): Long

    fun findByIdAndStatusTrue(id: Int): Asset?

    /*
        dirty checking, flush 과정을 거치지 않는 직접 쿼리
        대규모 DB 에서의 속도 보장
    */
    @Modifying
    @Query("UPDATE Asset a SET a.status = false WHERE a.id = :id")
    fun softDeleteById(id: Int)

    fun findAllByMemberId(memberId: Int): List<Asset>
}