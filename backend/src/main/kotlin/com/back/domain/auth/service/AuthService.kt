package com.back.domain.auth.service

import com.back.domain.auth.dto.FindAccountRequest
import com.back.domain.auth.dto.MemberLoginRequest
import com.back.domain.auth.dto.ResetPasswordRequest
import com.back.domain.member.entity.Member
import com.back.domain.member.exception.NotFoundMemberException
import com.back.domain.member.repository.MemberRepository
import com.back.domain.member.service.MemberService
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.security.SecureRandom

@Service
class AuthService(
    private val memberService: MemberService,
    private val memberRepository: MemberRepository,
    private val validateAuthService: ValidatePasswordService,
    ) {

    fun authenticateMember(request: MemberLoginRequest): Member {
        val member = memberService.findMemberByEmail(request.email)
        validateAuthService.validateCurrentPassword(member,request.password)

        return member
    }

    fun findAccount(request: FindAccountRequest) : Member{
        return memberRepository.findByNameAndPhoneNumberAndIsDeletedFalse(request.name, request.phoneNumber)
            ?: throw NotFoundMemberException("존재하지 않는 회원입니다.")
    }

}