package com.back.global.initData

import com.back.domain.member.entity.Member
import com.back.domain.member.entity.MemberRole
import com.back.domain.member.repository.MemberRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder

@Profile("test")
@Configuration
class TestInitData (
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @Bean
    fun initData() = ApplicationRunner {
        if (memberRepository.findByEmail("admin@test.com") == null) {
            memberRepository.save(
                Member(
                    "admin@test.com",
                    passwordEncoder.encode("admin123"),
                    "관리자",
                    "010-0000-0000",
                    MemberRole.ADMIN
                )
            )
        }

        if (memberRepository.findByEmail("user1@test.com") == null) {
            memberRepository.save(
                Member(
                    "user1@test.com",
                    passwordEncoder.encode("user123"),
                    "유저",
                    "010-1111-1111",
                    MemberRole.USER
                )
            )
        }
    }
}