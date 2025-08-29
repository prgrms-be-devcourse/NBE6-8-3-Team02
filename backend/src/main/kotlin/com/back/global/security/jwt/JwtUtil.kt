package com.back.global.security.jwt

import com.back.domain.member.entity.Member
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtUtil (private val jwtConfig: JwtConfig){
    private val logger = KotlinLogging.logger {}

    fun getSigningKey(): SecretKey = Keys.hmacShaKeyFor(jwtConfig.secretKey.toByteArray())

    fun generateToken(member: Member, tokenType: TokenType): String {
        val now = Date()
        val validity = when (tokenType) {
            TokenType.ACCESS_TOKEN -> jwtConfig.accessTokenValidity
            TokenType.REFRESH_TOKEN -> jwtConfig.refreshTokenValidity
        }
        val expiryDate = Date(now.time + validity)

        return Jwts.builder().apply{
            setSubject(member.email)
            claim("memberId",member.id)
            claim("role",member.role)
            setIssuedAt(now)
            setExpiration(expiryDate)
            signWith(getSigningKey())
        }.compact()
    }

    fun validateToken(token: String):Boolean {
        return try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
            true
        }catch(e:RuntimeException) {
            logger.error("토큰 검증에 실패하였습니다.")
            false
        }
    }

    fun getEmailFromToken(token:String):String{
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .payload
            .subject
    }
    fun getRoleFromToken(token:String):String{
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .payload["role"] as String
    }
}