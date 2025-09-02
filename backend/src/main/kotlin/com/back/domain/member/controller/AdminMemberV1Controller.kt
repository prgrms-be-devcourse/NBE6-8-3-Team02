package com.back.domain.member.controller

import com.back.domain.member.dto.AdminMemberResponse
import com.back.domain.member.extension.toAdminMemberResponse
import com.back.domain.member.service.MemberService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin/members")
@Tag(name = "Admin Member", description = "관리자 회원 관리 API")
class AdminMemberV1Controller(private val memberService: MemberService) {

    @GetMapping
    @Operation(summary = "전체 회원 조회", description = "탈퇴한 회원을 제외한 전체 회원을 조회합니다.")
    fun getAllMembers(): ResponseEntity<List<AdminMemberResponse>> {
        val response = memberService.getAllMembers()

        return ResponseEntity.ok(response)
    }

    @GetMapping("{memberId}")
    @Operation(summary = "회원 ID로 조회", description = "회원 ID로 회원 정보를 조회합니다.")
    fun getMember(@PathVariable memberId: Int): ResponseEntity<AdminMemberResponse> {
        val member = memberService.getMemberById(memberId)
        val response = member.toAdminMemberResponse()

        return ResponseEntity.ok(response)
    }

    @PatchMapping("/{memberId}/activate")
    @Operation(summary = "회원 활성화", description = "회원의 상태를 활성으로 변경합니다.")
    fun activateMember(@PathVariable memberId: Int): ResponseEntity<String> {
        memberService.activateMember(memberId)

        return ResponseEntity.ok("관리자가 회원 ${memberId}를 활성화 했습니다.")
    }

    @PatchMapping("/{memberId}/deactivate")
    @Operation(summary = "회원 비활성화", description = "회원의 상태를 비활성화로 변경합니다.")
    fun deactivateMember(@PathVariable memberId:Int): ResponseEntity<String>{
        memberService.deactivateMember(memberId)

        return ResponseEntity.ok("관리자가 회원 ${memberId}를 비활성화 했습니다.")
    }

    @GetMapping("/active")
    @Operation(summary="활성화된 회원 조회", description = "활성화된 회원 목록을 조회합니다.")
    fun getActiveMembers():ResponseEntity<List<AdminMemberResponse>>{
        val response = memberService.getActiveMembers()

        return ResponseEntity.ok(response)
    }

    @GetMapping("/search")
    @Operation(summary = "회원 검색", description = "이메일과 이름으로 회원을 검색합니다.")
    fun searchMember(
        @RequestParam
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        email: String,
        @Size(min = 2, max = 20, message = "이름은 2~20자 사이여야 합니다.")
        @RequestParam name: String
    ): ResponseEntity<AdminMemberResponse> {
        val member = memberService.getMemberByEmailAndName(email, name)
        val response = member.toAdminMemberResponse()

        return ResponseEntity.ok(response)
    }

}