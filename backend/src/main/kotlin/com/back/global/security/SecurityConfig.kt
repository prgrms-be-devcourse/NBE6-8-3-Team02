package com.back.global.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfig(private val jwtAuthenticationFilter: JwtAuthenticationFilter) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf{it.disable()}
            .cors { }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .headers { it.frameOptions { it.sameOrigin() } }
            .authorizeHttpRequests{
                it.requestMatchers(
                "/",
                "/favicon.ico",
                "/h2-console/**",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-ui.html",
                "/swagger-resources/**",
                "/webjars/**",
                "/api/v1/auth/**",
                "/api/v1/members/signup" ,
                "/api/v1/members/check-email"
            ).permitAll()
                // ADMIN 전용 - 관리자만 접근 가능
                .requestMatchers(HttpMethod.GET, "/api/v1/admin/members").hasRole("ADMIN")           // 전체 회원 조회
                .requestMatchers(HttpMethod.GET, "/api/v1/admin/members/active").hasRole("ADMIN")    // 활성 회원 조회
                .requestMatchers(HttpMethod.PATCH, "/api/v1/admin/members/{memberId}/activate").hasRole("ADMIN")    // 회원 활성화
                .requestMatchers(HttpMethod.PATCH, "/api/v1/admin/members/{memberId}/deactivate").hasRole("ADMIN")  // 회원 비활성화

                // USER - 인증된 사용자 (본인 데이터만)
                .requestMatchers("/api/v1/members/me").authenticated()                         // 본인 정보 조회
                .requestMatchers("/api/v1/members/search/**").authenticated()                  // 이메일/이름으로 조회
                .requestMatchers(HttpMethod.PATCH, "/api/v1/members").authenticated() // 본인 정보 수정
                .requestMatchers(HttpMethod.DELETE, "/api/v1/members/{memberId}").authenticated() // 본인 정보 삭제
                .requestMatchers(HttpMethod.PATCH, "/api/v1/members/{memberId}/password").authenticated() // 비밀번호 변경

                // 자산관리 관련 API - 인증된 사용자만
                .requestMatchers("/api/v1/assets/**").authenticated()        // 자산 관리
                .requestMatchers("/api/v1/accounts/**").authenticated()      // 계좌 관리
                .requestMatchers("/api/v1/transactions/**").authenticated()  // 거래내역 관리
                .requestMatchers("/api/v1/goals/**").authenticated()         // 목표 관리
                .requestMatchers("/api/v1/snapshot/**").authenticated()      // 스냅샷 관리

                // 공지사항 관련 API - 조회는 모두 허용, 생성/수정/삭제는 관리자만
                .requestMatchers(HttpMethod.GET, "/api/v1/notices/**").permitAll()           // 공지사항 조회 (모든 사용자)
                .requestMatchers(HttpMethod.POST, "/api/v1/notices/**").hasRole("ADMIN")     // 공지사항 생성 (관리자만)
                .requestMatchers(HttpMethod.PUT, "/api/v1/notices/**").hasRole("ADMIN")      // 공지사항 수정 (관리자만)
                .requestMatchers(HttpMethod.DELETE, "/api/v1/notices/**").hasRole("ADMIN")   // 공지사항 삭제 (관리자만)

                .anyRequest().denyAll()
                }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)// 나머지는 모두 차단

        return http.build()
    }


    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun corsConfigurationSource(): UrlBasedCorsConfigurationSource {
        val configuration= CorsConfiguration()

        configuration.setAllowedOrigins(mutableListOf<String?>("http://localhost:3000"))
        configuration.setAllowedMethods(mutableListOf<String?>("GET", "POST", "PUT", "PATCH", "DELETE"))
        configuration.setAllowCredentials(true)
        configuration.setAllowedHeaders(mutableListOf<String?>("*"))

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)

        return source
    }
}
