package com.back.domain.member.service

import com.back.domain.member.dto.AdminMemberResponse
import com.back.domain.member.dto.MemberDetailsUpdateRequest
import com.back.domain.member.dto.MemberDetailsUpdateResponse
import com.back.domain.member.dto.MemberPasswordChangeRequest
import com.back.domain.member.dto.MemberSignUpRequest
import com.back.domain.member.dto.MemberSignUpResponse
import com.back.domain.member.entity.Member
import com.back.domain.member.exception.DuplicateEmailException
import com.back.domain.member.exception.NotFoundMemberException
import com.back.domain.member.extension.toAdminMemberResponse
import com.back.domain.member.extension.toMember
import com.back.domain.member.extension.toMemberDetailsUpdateResponse
import com.back.domain.member.extension.toMemberSignUpResponse
import com.back.domain.member.repository.MemberRepository
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class MemberService(private val memberRepository: MemberRepository,
                    private val passwordEncoder: PasswordEncoder) {

    @Transactional
    fun join(request: MemberSignUpRequest): MemberSignUpResponse {
        if(memberRepository.existsByEmailAndIsDeletedFalse(request.email)){
            throw DuplicateEmailException("이미 가입된 이메일입니다.")
        }

        val encodePassword = passwordEncoder.encode(request.password)
        val member = request.toMember(encodePassword)

        return memberRepository.save(member).toMemberSignUpResponse()
    }

    fun findMemberByEmail(email:String): Member{
        return memberRepository.findByEmail(email)?:
        throw NotFoundMemberException("존재하지 않는 회원입니다.")
    }


    @Transactional
    fun updateMemberDetails(
        authMember: Member,
        request: MemberDetailsUpdateRequest
    ): MemberDetailsUpdateResponse {
        val member = findMemberByEmail(authMember.email)

        request.name?.let { newName ->
            member.updateName(newName)}
        request.phoneNumber?.let { newPhone ->
            member.updatePhoneNumber(newPhone)
        }

        return member.toMemberDetailsUpdateResponse()
    }

    @Transactional
    fun softDeleteMember(authMember: Member){
        val member = findMemberByEmail(authMember.email)

        member.softDelete()
    }

    @Transactional
    fun changePassword(
        authMember:Member,
        request: MemberPasswordChangeRequest
    ){
        val member = findMemberByEmail(authMember.email)

        member.updatePassword(request.currentPassword,request.newPassword, passwordEncoder)
    }

    fun isEmailDuplicate(email:String):Boolean{
        return memberRepository.existsByEmailAndIsDeletedFalse(email)
    }

    fun getAllMembers():List<AdminMemberResponse>{
        val members = memberRepository.findByIsDeletedFalse()

        return members.map{ member->
            member.toAdminMemberResponse()
        }
    }

    fun getMemberById(memberId:Int):Member{
        val member = memberRepository.findByIdAndIsDeletedFalse(memberId) ?:
        throw NotFoundMemberException("존재하지 않는 회원입니다.")

        return member
    }

    @Transactional
    fun activateMember(memberId:Int){
        val member = getMemberById(memberId)

        member.activate()
    }

    @Transactional
    fun deactivateMember(memberId:Int){
        val member = getMemberById(memberId)

        member.deactivate()
    }

    fun getActiveMembers(): List<AdminMemberResponse> {
        val members = memberRepository.findByIsActiveTrueAndIsDeletedFalse()

        return members.map { member ->
            member.toAdminMemberResponse()
        }
    }

    fun getMemberByEmailAndName(email: String, name: String): Member {
        return memberRepository.findByEmailAndNameAndIsDeletedFalse(email, name)
            ?: throw NotFoundMemberException("존재하지 않는 회원입니다.")
    }
}