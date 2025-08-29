package com.back.domain.auth.service

import com.back.domain.auth.dto.MemberLoginRequest
import com.back.domain.member.entity.Member
import com.back.domain.member.exception.NotFoundMemberException
import com.back.domain.member.exception.PasswordMisMatchException
import com.back.domain.member.service.MemberService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val memberService: MemberService, private val passwordEncode: PasswordEncoder
) {

    fun authenticateMember(request: MemberLoginRequest): Member {
        val member = memberService.findMemberByEmail(request.email)
            ?: throw NotFoundMemberException("존재하지 않는 이메일입니다.")


        if (!passwordEncode.matches(request.password,member.password)) {
            throw PasswordMisMatchException("비밀번호가 일치하지 않습니다.")
        }

        return member
    }
}