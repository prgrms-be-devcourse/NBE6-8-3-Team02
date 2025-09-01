package com.back.domain.auth.service

import com.back.domain.member.entity.Member
import com.back.domain.member.exception.PasswordMisMatchException
import com.back.domain.member.exception.UnchangedMemberDetailsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class ValidatePasswordService(private val passwordEncoder: PasswordEncoder) {

    fun validateNewPassword(member: Member, newPassword: String) {
        if (passwordEncoder.matches(
                newPassword,
                member.password
            )
        ) {
            throw UnchangedMemberDetailsException("같은 비밀번호로 수정할 수 없습니다.")
        }
    }

    fun validateCurrentPassword(member: Member, currentPassword: String) {
        if (!passwordEncoder.matches(
                currentPassword,
                member.password
            )
        ) {
            throw PasswordMisMatchException("현재 비밀번호가 일치하지 않습니다.")
        }
    }
}