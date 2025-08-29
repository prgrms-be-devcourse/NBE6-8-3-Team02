package com.back.domain.member.extension

import com.back.domain.auth.dto.MemberLoginResponse
import com.back.domain.member.dto.MemberDetailsUpdateResponse
import com.back.domain.member.dto.MemberSignUpRequest
import com.back.domain.member.dto.MemberSignUpResponse
import com.back.domain.member.entity.Member
import com.back.domain.member.entity.MemberRole

fun Member.toMemberSignUpResponse() = MemberSignUpResponse(
    memberId = this.id,
    email = this.email,
    name = this.name,
    phoneNumber = this.phoneNumber,
    role = this.role,
    isActive = this.isActive,
    createDate = this.createDate,
    modifyDate = this.modifyDate
)

fun MemberSignUpRequest.toMember(encryptedPassword: String) = Member(
    email = this.email,
    password = encryptedPassword,
    name = this.name,
    phoneNumber = this.phoneNumber,
    role = MemberRole.USER
)

fun Member.toMemberLoginResponse() = MemberLoginResponse(
    memberId = this.id,
    email = this.email,
    name = this.name,
    role = this.role
)

fun Member.toMemberDetailsUpdateResponse() = MemberDetailsUpdateResponse(
    memberId = this.id,
    email = this.email,
    name = this.name,
    phoneNumber = this.phoneNumber,
    role = this.role.name,
    isActive = this.isActive,
    createDate = this.createDate,
    modifyDate = this.modifyDate
    )