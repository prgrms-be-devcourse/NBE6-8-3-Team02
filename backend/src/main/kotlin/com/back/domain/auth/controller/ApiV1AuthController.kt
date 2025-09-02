package com.back.domain.auth.controller

import com.back.domain.auth.dto.*
import com.back.domain.auth.service.AuthService
import com.back.domain.auth.service.ResetPasswordService
import com.back.domain.member.exception.NotFoundMemberException
import com.back.domain.member.extension.toFindAccountResponse
import com.back.domain.member.extension.toMemberLoginResponse
import com.back.domain.member.service.MemberService
import com.back.global.security.jwt.JwtCookieUtil
import com.back.global.security.jwt.JwtUtil
import com.back.global.security.jwt.TokenType
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
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
    private val resetPasswordService: ResetPasswordService,
    private val jwtCookie: JwtCookieUtil,
    private val jwtUtil: JwtUtil,
    private val memberService: MemberService,
) {

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인 후 JWT 토큰을 받아 쿠키에 전달")
    @PostMapping("login")
    fun login(@Valid @RequestBody request: MemberLoginRequest)
            : ResponseEntity<MemberLoginResponse> {
        val member = authService.authenticateMember(request)

        val accessToken = jwtUtil.generateToken(member, TokenType.ACCESS_TOKEN)
        val refreshToken = jwtUtil.generateToken(member, TokenType.REFRESH_TOKEN)

        val accessTokenCookie = jwtCookie.createJwtCookie(TokenType.ACCESS_TOKEN, accessToken, false)
        val refreshTokenCookie = jwtCookie.createJwtCookie(TokenType.REFRESH_TOKEN, refreshToken, false)

        return ResponseEntity.ok()
            .header("Set-Cookie", accessTokenCookie.toString())
            .header("Set-Cookie", refreshTokenCookie.toString())
            .body(member.toMemberLoginResponse())
    }

    @Operation(summary = "로그아웃", description = "JWT 토큰이 담긴 쿠키를 삭제합니다.")
    @PostMapping
    fun logout(): ResponseEntity<Void> {
        val deleteAccessTokenCookie = jwtCookie.createJwtCookie(TokenType.ACCESS_TOKEN, "", true)
        val deleteRefreshTokenCookie = jwtCookie.createJwtCookie(TokenType.REFRESH_TOKEN, "", true)

        return ResponseEntity.noContent()
            .header("Set-Cookie", deleteAccessTokenCookie.toString())
            .header("Set-Cookie", deleteRefreshTokenCookie.toString())
            .build()
    }

    @PostMapping("/find-account")
    @Operation(summary = "계정 찾기", description = "이름과 전화번호로 이메일을 찾습니다.")
    fun findAccount(
        @Valid @RequestBody request: FindAccountRequest,
    ): ResponseEntity<FindAccountResponse> {
        val member = authService.findAccount(request)
        val response = member.toFindAccountResponse()

        return ResponseEntity.ok(response)
    }

    @PostMapping("/reset-password")
    @Operation(summary = "비밀번호 재설정", description = "이메일, 이름, 전화번호 확인 후 비밀번호를 재설정합니다.")
    fun resetPassword(@Valid @RequestBody request: ResetPasswordRequest): ResponseEntity<String> {
        resetPasswordService.resetPassword(request)

        return ResponseEntity.ok("임시 비밀번호가 발급되었습니다.")
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 갱신", description = "Refresh Token을 사용하여 새로운 Access Token을 발급받습니다.")
    fun resetAccessToken(request: HttpServletRequest): ResponseEntity<String>{
        val refreshToken =
            jwtCookie.extractRefreshTokenFromCookie(request) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Refresh Token이 없습니다.")

        if(!jwtUtil.validateToken(refreshToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("유효하지 않은 Refresh Token입니다.")
        }

        val member = runCatching {
            val email = jwtUtil.getEmailFromToken(refreshToken)
            memberService.findMemberByEmail(email)
        }.getOrElse { throw NotFoundMemberException("사용자를 찾을 수 없습니다.") }

        val newAccessToken = jwtUtil.generateToken(member, TokenType.ACCESS_TOKEN)
        val newRefreshToken = jwtUtil.generateToken(member, TokenType.REFRESH_TOKEN)

        val accessTokenCookie = jwtCookie.createJwtCookie(TokenType.ACCESS_TOKEN, newAccessToken, false)
        val refreshTokenCookie = jwtCookie.createJwtCookie(TokenType.REFRESH_TOKEN, newRefreshToken, false)

        return ResponseEntity.ok()
            .header("Set-Cookie", accessTokenCookie.toString())
            .header("Set-Cookie", refreshTokenCookie.toString())
            .body("새로운 Access, Refresh 토큰이 발급되었습니다.")
    }


}