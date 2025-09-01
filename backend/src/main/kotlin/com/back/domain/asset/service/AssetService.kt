package com.back.domain.asset.service

import com.back.domain.asset.dto.CreateAssetRequestDto
import com.back.domain.asset.dto.CreateWithoutMemberDto
import com.back.domain.asset.dto.UpdateAssetRequestDto
import com.back.domain.asset.entity.Asset
import com.back.domain.asset.entity.AssetType
import com.back.domain.asset.repository.AssetRepository
import com.back.domain.member.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AssetService(
    private val assetRepository: AssetRepository,
    private val memberRepository: MemberRepository
) {
    // 엔티티 빌더
    @Transactional
    fun createAsset(createAssetRequestDto: CreateAssetRequestDto): Asset {
        val member = memberRepository.findById(createAssetRequestDto.memberId)
            .orElseThrow { NoSuchElementException("존재하지 않는 회원입니다.") }

        val asset = Asset(
            member = member,
            name = createAssetRequestDto.name,
            assetType = AssetType.valueOf(createAssetRequestDto.assetType),
            assetValue = createAssetRequestDto.assetValue,
            status = true
        )

        return assetRepository.save(asset)
    }

    @Transactional
    fun createAssetByMember(memberId: Int, createWithoutMemberDto: CreateWithoutMemberDto): Asset {
        val member = memberRepository.findById(memberId)
            .orElseThrow { NoSuchElementException("존재하지 않는 회원입니다.") }

        val asset = Asset(
            member = member,
            name = createWithoutMemberDto.name,
            assetType = AssetType.valueOf(createWithoutMemberDto.assetType),
            assetValue = createWithoutMemberDto.assetValue,
            status = true
        )

        return assetRepository.save(asset)
    }

    // ------- 일반 서비스 -------- //
    @Transactional(readOnly = true)
    fun count(): Long {
        return assetRepository.countAllByStatusTrue()
    }

    @Transactional(readOnly = true)
    fun findById(id: Int): Asset {
        return assetRepository.findByIdAndStatusTrue(id)
            ?: throw NoSuchElementException("해당 id는 존재하지 않는 자산입니다. id: $id")
    }

    @Transactional(readOnly = true)
    fun findAll(): List<Asset> {
        return assetRepository.findAllByStatusTrue()
    }

    @Transactional(readOnly = true)
    fun findAllByMemberId(memberId: Int): List<Asset> {
        return assetRepository.findAllByStatusTrueAndMemberId(memberId)
    }

    @Transactional
    fun deleteById(id: Int): Asset {
        val asset = assetRepository.findByIdAndStatusTrue(id)
            ?: throw NoSuchElementException("해당 id는 존재하지 않는 자산입니다. id: $id")

        assetRepository.softDeleteById(id)
        return asset
    }

    @Transactional
    fun updateById(updateAssetRequestDto: UpdateAssetRequestDto): Asset {
        val asset = assetRepository.findById(updateAssetRequestDto.id)
            .orElseThrow{ NoSuchElementException("해당 id는 존재하지 않는 자산입니다. id: ${updateAssetRequestDto.id}") }

        asset.name = updateAssetRequestDto.name
        asset.assetType = AssetType.valueOf(updateAssetRequestDto.assetType)
        asset.assetValue = updateAssetRequestDto.assetValue

        return assetRepository.save(asset)
    }

    @Transactional(readOnly = true)
    fun getAssetsByMemberId(memberId: Int): List<Asset> {
        return assetRepository.findAllByMemberId(memberId)
    }

    @Transactional
    fun flush() {
        assetRepository.flush()
    }
}