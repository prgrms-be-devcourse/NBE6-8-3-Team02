package com.back.global.security

import com.back.domain.member.entity.Member
import com.back.domain.member.exception.NotFoundMemberException
import com.back.domain.member.repository.MemberRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomMemberDetailsService(private val memberRepository: MemberRepository) : UserDetailsService {

    override fun loadUserByUsername(email: String): CustomMemberDetails {
        val member: Member = memberRepository.findByEmail(email)?:
            throw NotFoundMemberException("존재하지 않는 회원입니다.")


        if(member.isDeleted){
            throw NotFoundMemberException("존재하지 않는 회원입니다.")
        }

        return CustomMemberDetails(member)
    }
}