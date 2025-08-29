package com.back.domain.member.controller

import com.back.domain.member.dto.MemberDetailsUpdateRequest
import com.back.domain.member.dto.MemberDetailsUpdateResponse
import com.back.domain.member.dto.MemberSignUpRequest
import com.back.domain.member.dto.MemberSignUpResponse
import com.back.domain.member.service.MemberService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/members")
@Tag(name = "Member", description = "회원 관련 API")
class ApiV1MemberController(private val memberService: MemberService) {

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "새로운 회원을 등록합니다.")
    fun signUp(@RequestBody request: MemberSignUpRequest): ResponseEntity<MemberSignUpResponse> {
        val response = memberService.join(request)

        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

//    @PatchMapping
//    @Operation(summary = "회원정보 수정", description = "회원 정보를 수정합니다.")
//    fun updateMember(@AuthenticationPrincipal memberDetails:CustomMemberDetails,
//                     @RequestBody request: MemberDetailsUpdateRequest
//    ): ResponseEntity<MemberDetailsUpdateResponse>{
//
//        val member = memberDetails.getMember()
//        val response = memberService.updateMemberDetails(member, request)
//
//        return ResponseEntity.ok(response)
//    }

}