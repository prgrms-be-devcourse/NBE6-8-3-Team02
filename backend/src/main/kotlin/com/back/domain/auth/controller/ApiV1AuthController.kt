package com.back.domain.auth.controller

import com.back.domain.auth.dto.MemberLoginRequest
import com.back.domain.auth.dto.MemberLoginResponse
import com.back.domain.auth.service.AuthService
import com.back.domain.member.extension.toMemberLoginResponse
import com.back.global.security.jwt.JwtUtil
import com.back.global.security.jwt.TokenType
import com.back.global.util.JwtCookie
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
class ApiV1AuthController(
    private val authService: AuthService,
    private val jwtCookie : JwtCookie,
    private val jwtUtil: JwtUtil
) {

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인 후 JWT 토큰을 받아 쿠키에 전달")
    @PostMapping("login")
    fun login(@RequestBody request: MemberLoginRequest)
    :ResponseEntity<MemberLoginResponse>{
        val member = authService.authenticateMember(request)

        val accessToken = jwtUtil.generateToken(member, TokenType.ACCESS_TOKEN)
        val refreshToken = jwtUtil.generateToken(member, TokenType.REFRESH_TOKEN)

        val accessTokenCookie = jwtCookie.createJwtCookie(TokenType.ACCESS_TOKEN, accessToken)
        val refreshTokenCookie = jwtCookie.createJwtCookie(TokenType.REFRESH_TOKEN, refreshToken)

        return ResponseEntity.ok()
            .header("Set-Cookie",accessTokenCookie.toString())
            .header("Set-Cookie",refreshTokenCookie.toString())
            .body(member.toMemberLoginResponse())
    }



}