package com.back.global.util

import com.back.global.security.jwt.JwtConfig
import com.back.global.security.jwt.TokenType
import jakarta.servlet.http.Cookie
import org.springframework.http.CacheControl.maxAge
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component

@Component
class JwtCookie(private val jwtConfig: JwtConfig) {

    fun createJwtCookie(tokenType: TokenType, token:String): ResponseCookie {
        val (cookieName,maxAge) = when (tokenType) {
            TokenType.ACCESS_TOKEN -> Pair("accessToken",
                jwtConfig.accessTokenValidity)
            TokenType.REFRESH_TOKEN -> Pair("refreshToken",jwtConfig.refreshTokenValidity)
        }
        return ResponseCookie.from(cookieName, token)
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(maxAge)
            .sameSite("Lax")
            .build()
    }
}