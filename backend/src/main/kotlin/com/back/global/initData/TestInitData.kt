package com.back.global.initData

import com.back.domain.account.entity.Account
import com.back.domain.account.repository.AccountRepository
import com.back.domain.asset.entity.Asset
import com.back.domain.asset.entity.AssetType
import com.back.domain.asset.repository.AssetRepository
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
    private val assetRepository: AssetRepository,
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @Bean
    fun initData() = ApplicationRunner {
        if (memberRepository.findByEmail("admintest@test.com") == null) {
            memberRepository.save(
                Member(
                    "admintest@test.com",
                    passwordEncoder.encode("admin123"),
                    "관리자",
                    "010-0000-0000",
                    MemberRole.ADMIN
                )
            )
        }

        if (memberRepository.findByEmail("usertest@test.com") == null) {
            memberRepository.save(
                Member(
                    "usertest@test.com",
                    passwordEncoder.encode("user123"),
                    "유저",
                    "010-1111-1111",
                    MemberRole.USER
                )
            )
        }

        val user = memberRepository.findByEmail("usertest@test.com")!!
        val asset = Asset(user, "자산", AssetType.DEPOSIT, 140000)
        assetRepository.save(asset)

        val account = Account(user, "{계좌 번호}", 1000L, "계좌")
        accountRepository.save(account)
    }
}