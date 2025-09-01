package com.back.domain.auth.service

import com.back.domain.auth.dto.ResetPasswordRequest
import com.back.domain.member.exception.NotFoundMemberException
import com.back.domain.member.repository.MemberRepository
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.security.SecureRandom

@Service
class ResetPasswordService
    (
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder

) {
    @Transactional
    fun resetPassword(request: ResetPasswordRequest){
        val member = memberRepository.findByEmailAndNameAndPhoneNumberAndIsDeletedFalse(
            request.email,
            request.name,
            request.phoneNumber
        )
            ?: throw NotFoundMemberException("존재하지 않는 회원입니다.")

        val temporaryPassword: String = generateTemporaryPassword()
        val encodedPassword: String = passwordEncoder.encode(temporaryPassword)

        member.updatePassword(encodedPassword)
    }

    fun generateTemporaryPassword(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val random = SecureRandom()

        return buildString {
            repeat(8) {
                append(chars[random.nextInt(chars.length)])
            }
        }
    }
}