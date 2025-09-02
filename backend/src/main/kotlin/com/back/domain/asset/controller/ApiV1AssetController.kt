package com.back.domain.asset.controller

import com.back.domain.asset.dto.AssetDto
import com.back.domain.asset.dto.CreateAssetRequestDto
import com.back.domain.asset.dto.CreateWithoutMemberDto
import com.back.domain.asset.dto.UpdateAssetRequestDto
import com.back.domain.asset.service.AssetService
import com.back.global.rsData.RsData
import com.back.global.security.CustomMemberDetails
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/assets")
@Tag(name = "ApiV1AssetController", description = "자산 컨트롤러")
class ApiV1AssetController(
    private val assetService: AssetService
) {
    // 생성
    @PostMapping
    @Operation(summary = "자산 등록")
    fun createAsset(
        @RequestBody createAssetRequestDto: CreateAssetRequestDto
    ): ResponseEntity<RsData<AssetDto>> {
        val asset = assetService.createAsset(createAssetRequestDto)
        val assetDto = AssetDto(asset)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(RsData("201-1", "자산이 등록되었습니다.", assetDto))
    }

    // 다건 조회
    @GetMapping
    @Operation(summary = "자산 다건 조회")
    fun getAssets(): ResponseEntity<RsData<List<AssetDto>>> {
        val assets = assetService.findAll()
        val assetDtos = assets.map { asset -> AssetDto(asset) }
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(RsData("200-1", "자산 목록을 조회했습니다.", assetDtos))
    }

    // 단건 조회
    @GetMapping("/{id}")
    @Operation(summary = "자산 단건 조회")
    fun getAsset(
        @PathVariable id: Int
    ): ResponseEntity<RsData<AssetDto>> {
        val asset = assetService.findById(id)
        val assetDto = AssetDto(asset)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(RsData("200-1", "${id}번 자산을 조회했습니다.", assetDto))
    }

    // id 기반 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "자산 삭제 (id 기반)")
    fun deleteAsset(
        @PathVariable id: Int
    ): ResponseEntity<RsData<AssetDto>> {
        val asset = assetService.deleteById(id)
        val assetDto = AssetDto(asset)
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(RsData("204-1", "${id}번 자산을 삭제했습니다.", assetDto))
    }

    // id 기반 수정
    @PutMapping("/{id}")
    @Operation(summary = "자산 수정 (id 기반)")
    fun updateAsset(
        @RequestBody updateAssetRequestDto: UpdateAssetRequestDto
    ): ResponseEntity<RsData<AssetDto>> {
        val asset = assetService.updateById(updateAssetRequestDto)
        val assetDto = AssetDto(asset)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(RsData("200-1", "${updateAssetRequestDto.id}번 자산을 수정했습니다.", assetDto))
    }

    @GetMapping("/member")
    @Operation(summary = "사용자 기반 자산 목록 조회")
    fun getAssetsByCurrentMember(
        @AuthenticationPrincipal userDetails: CustomMemberDetails
    ): ResponseEntity<RsData<List<AssetDto>>> {
        val memberId = userDetails.getMember().id
        val assets = assetService.findAllByMemberId(memberId)
        val assetDtos = assets.map { asset -> AssetDto(asset) }
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(RsData("200-1", "${memberId}번 사용자의 자산 목록을 조회했습니다.", assetDtos))
    }

    @PostMapping("/member")
    @Operation(summary = "사용자 기반 자산 등록")
    fun createAssetByCurrentMember(
        @AuthenticationPrincipal userDetails: CustomMemberDetails,
        @RequestBody createWithoutMemberDto: CreateWithoutMemberDto
    ): ResponseEntity<RsData<AssetDto>> {
        val memberId = userDetails.getMember().id
        val asset = assetService.createAssetByMember(memberId, createWithoutMemberDto)
        val assetDto = AssetDto(asset)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(RsData("200-1", "자산이 등록되었습니다.", assetDto))
    }
}