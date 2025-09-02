package com.back.domain.member.service

import com.back.domain.member.dto.MemberDetailsUpdateRequest
import com.back.domain.member.entity.Member
import com.back.domain.member.entity.MemberRole
import com.back.domain.member.repository.MemberRepository
import com.back.global.jpa.entity.BaseEntity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.springframework.security.crypto.password.PasswordEncoder
import kotlin.test.Test
import io.mockk.mockk
import java.time.LocalDate
import java.time.LocalDateTime

//class MemberServiceTest {
//
//    val memberRepository: MemberRepository = mockk()
//    val passwordEncoder: PasswordEncoder = mockk()
//
//    val memberService = MemberService(memberRepository, passwordEncoder)
//    @Test
//    @DisplayName("회원정보 수정 테스트")
//    fun updateMemberDetails(){
//        val member = Member(
//            email = "admin@example.com",
//            password = "admin123",
//            name = "user1",
//            phoneNumber = "010-9999-8888",
//            role = MemberRole.ADMIN,
//            isActive = false,
//        )
//        member.createDate= LocalDateTime.now()
//        member.modifyDate= LocalDateTime.now()
//        val request = MemberDetailsUpdateRequest("관리자")
//
//        val result= memberService.updateMemberDetails(member,request)
//
//        assertEquals("관리자",result.name)
//    }
//}