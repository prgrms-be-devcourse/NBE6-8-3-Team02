package com.back.global.security.jwt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "jwt")
class JwtConfig {
    lateinit var secretKey: String
    var accessTokenValidity: Long = 0
    var refreshTokenValidity: Long = 0
}