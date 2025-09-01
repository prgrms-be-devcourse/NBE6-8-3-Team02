package com.back.global.security.jwt

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.CacheControl.maxAge
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component

@Component
class JwtCookieUtil(private val jwtConfig: JwtConfig) {

    fun createJwtCookie(tokenType: TokenType, token: String, maxAgeZero: Boolean): ResponseCookie {
        val cookieName = when (tokenType) {
            TokenType.ACCESS_TOKEN -> "accessToken"
            TokenType.REFRESH_TOKEN -> "refreshToken"
        }

        val maxAge :Long = if (maxAgeZero){
            0
        }else{
            when (tokenType) {
                TokenType.ACCESS_TOKEN -> jwtConfig.accessTokenValidity
                TokenType.REFRESH_TOKEN ->jwtConfig.refreshTokenValidity
            }
        }
        return ResponseCookie.from(cookieName, token)
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(maxAge)
            .sameSite("Lax")
            .build()
    }

    fun extractRefreshTokenFromCookie(request: HttpServletRequest):String?{
        return request.cookies
            ?.firstOrNull { it.name == "refreshToken" }
            ?.value
    }
}