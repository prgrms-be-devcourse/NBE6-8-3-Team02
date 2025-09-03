package com.back.domain.member.service

import com.back.domain.auth.service.ValidatePasswordService
import com.back.domain.member.dto.MemberDetailsUpdateRequest
import com.back.domain.member.entity.Member
import com.back.domain.member.entity.MemberRole
import com.back.domain.member.repository.MemberRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime
import kotlin.test.Test

class MemberServiceTest {

    private val memberRepository: MemberRepository = mockk(relaxed = true)
    private val passwordEncoder: PasswordEncoder = mockk(relaxed = true)
    private val validatePasswordService: ValidatePasswordService = mockk(relaxed = true)

    private val memberService = MemberService(memberRepository, passwordEncoder, validatePasswordService)

    @Test
    @DisplayName("회원정보 수정 테스트")
    fun updateMemberDetails() {
        // given
        val member = Member(
            email = "admintest@test.com",
            password = "admin123",
            name = "user1",
            phoneNumber = "010-9999-8888",
            role = MemberRole.ADMIN,
            isActive = false,
        ).apply {
            createDate = LocalDateTime.now()
            modifyDate = LocalDateTime.now()
        }

        val request = MemberDetailsUpdateRequest("관리자")

        // Repository mock 동작 정의 (update 후 저장되는 경우)
        every { memberRepository.findByEmail("admintest@test.com") } returns member

        // when
        val result = memberService.updateMemberDetails(member, request)

        // then
        assertEquals("관리자", result.name)
    }
}