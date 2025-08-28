package com.back.domain.asset.controller

import com.back.domain.asset.dto.AssetDto
import com.back.domain.asset.dto.CreateAssetRequestDto
import com.back.domain.asset.entity.Asset
import com.back.domain.asset.service.AssetService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/assets")
@Tag(name = "ApiV1AssetController", description = "자산 컨트롤러")
class ApiV1AssetController(
    private val assetService: AssetService
) {
    // 생성

}