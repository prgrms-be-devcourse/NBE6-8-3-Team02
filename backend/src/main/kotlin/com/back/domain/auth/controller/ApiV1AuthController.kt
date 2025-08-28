package com.back.domain.auth.controller

import com.back.domain.auth.dto.MemberLoginRequest
import com.back.domain.auth.dto.MemberLoginResponse
import com.back.domain.auth.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Auth", description = "인증 관련 API")
@RequestMapping("/api/v1/auth")
class ApiV1AuthController(private val authService: AuthService) {

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인 후 JWT 토큰을 받아 쿠키에 전달")
    @PostMapping("login")
    fun login(@RequestBody request: MemberLoginRequest)
    :ResponseEntity<MemberLoginResponse>{
        val response = authService.authenticateMember(request)

        //토큰 발급 로직

        return ResponseEntity.ok(response)
    }

}