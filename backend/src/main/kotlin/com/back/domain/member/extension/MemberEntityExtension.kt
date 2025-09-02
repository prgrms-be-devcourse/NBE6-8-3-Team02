package com.back.domain.member.extension

import com.back.domain.auth.dto.FindAccountResponse
import com.back.domain.auth.dto.MemberLoginResponse
import com.back.domain.member.dto.*
import com.back.domain.member.dto.AdminMemberResponse.Companion.maskEmail
import com.back.domain.member.dto.AdminMemberResponse.Companion.maskName
import com.back.domain.member.dto.AdminMemberResponse.Companion.maskPhone
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

fun Member.toMemberResponse() = MemberResponse(
    memberId = this.id,
    email = this.email,
    name = this.name,
    phoneNumber = this.phoneNumber,
    role = this.role.name,
    isActive = this.isActive,
    createDate = this.createDate,
    modifyDate = this.modifyDate
)

fun Member.toAdminMemberResponse() = AdminMemberResponse(
    memberId = this.id,
    maskedEmail = this.email.maskEmail(),
    maskedName = this.name.maskName(),
    maskedPhone = this.phoneNumber.maskPhone(),
    status = if (this.isActive) "ACTIVE" else "INACTIVE",
    createDate = this.createDate,
    modifyDate = this.modifyDate
)

fun Member.toFindAccountResponse() = FindAccountResponse(
    name = this.name,
    email = this.email
)